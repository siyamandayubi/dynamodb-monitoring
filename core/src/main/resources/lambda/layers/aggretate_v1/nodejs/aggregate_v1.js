String.prototype.replaceAll = function (search, replace) {
    return this.split(search).join(replace);
}

const getOrCreateGroupValueObj = (table, groupValue) => {
    var existingGroup = table.groups.filter((g) => g.groupValue.value == groupValue.value);

    const groupValueObj = existingGroup.length > 0 ? existingGroup[0] : {items: []};
    if (existingGroup.length == 0) {
        table.groups.push(groupValueObj);
    }
    groupValueObj.groupValue = groupValue;
    return groupValueObj;
};

const getOrCreateItem = (groupValueObj, hashValue, field) => {
    let existingItems = groupValueObj.items.filter((x) => x.hash == hashValue);
    let item;
    if (existingItems.length == 0) {
        item = {
            isNew: true,
            fieldType: null,
            hash: hashValue,
            fieldName: field.fieldName,
            fieldValue: null,
            insertCount: 0,
            deleteCount: 0,
            updateCount: 0,
            minItemCount: null,
            sum: 0,
            maxItemCount: null,
            max: null,
            min: null
        };

        groupValueObj.items.push(item);
    } else {
        item = existingItems[0];
    }
    return item;
}

const fillTables = (tables, fieldReferences, records, hashFunction) => {
    if (records == null || !Array.isArray(records)) {
        return;
    }

    if (fieldReferences.groups == null || !Array.isArray(fieldReferences.groups)) {
        return;
    }

    records.forEach(record => {
        let newData = record.dynamodb.NewImage;
        let oldData = record.dynamodb.OldImage;
        fieldReferences.groups.forEach(group => {

            if (group.fields == null || !Array.isArray(group.fields)) {
                return;
            }

            let table = tables[group.tableName];

            table.groups = table.groups || [];
            if (
                (newData && oldData && newData[group.fieldName] == null && oldData[group.fieldName] == null) ||
                (newData && !oldData && newData[group.fieldName] == null) ||
                (!newData && oldData && oldData[group.fieldName] == null) ||
                (!newData && !oldData)) {
                return;
            }

            let groupValue = getFieldValueAndType(newData ? newData : oldData, group.fieldName, group.path);
            let oldGroupValue = getFieldValueAndType(oldData, group.fieldName, group.path);
            group.fields.forEach(field => {
                let hashSource = groupValue.value + ":" + field.fieldName;
                let hashValue = hashFunction(hashSource);
                const groupValueObj = getOrCreateGroupValueObj(table, groupValue);

                let item = getOrCreateItem(groupValueObj, hashValue, field);

                if (record.eventName == "INSERT") {
                    item.insertCount++;
                    handleChangeEvent(item, field, newData, oldData);
                }

                if (record.eventName == "MODIFY") {
                    let groupChanged = false;
                    if (oldGroupValue != null && oldGroupValue.value != groupValue.value) {
                        groupChanged = true;
                        const oldGroupValueObj = getOrCreateGroupValueObj(table, oldGroupValue);
                        let oldHashSource = oldGroupValue.value + ":" + field.fieldName;
                        let oldHashValue = hashFunction(oldHashSource);
                        let modifiedItem = getOrCreateItem(oldGroupValueObj, oldHashValue, field);

                        let oldFieldValue = getFieldValueAndType(oldData, field.fieldName, field.path);
                        modifiedItem.deleteCount++;
                        if (oldFieldValue != null && oldFieldValue.fieldType == "N") {
                            modifiedItem.sum = modifiedItem.sum || 0;
                            modifiedItem.sum -= parseFloat(oldFieldValue.value);
                        }
                    }
                    item.updateCount++;
                    let newFieldValue = getFieldValueAndType(newData, field.fieldName, field.path);
                    let oldFieldValue = getFieldValueAndType(oldData, field.fieldName, field.path);
                    if (!groupChanged && newFieldValue != null && oldFieldValue != null && newFieldValue.value == oldFieldValue.value) {
                        return;
                    }
                    handleChangeEvent(item, field, newData, oldData);
                }

                if (record.eventName == "REMOVE") {
                    item.deleteCount++;
                }
            });
        });
    })
};

const handleChangeEvent = (item, field, newData, oldData) => {
    if (newData[field.fieldName] != null) {
        let newFieldValue = getFieldValueAndType(newData, field.fieldName, field.path);

        if (item.isNew) {
            item.fieldType = newFieldValue.type;
            item.fieldValue = newFieldValue.value;
            item.minItemCount = newFieldValue.length;
            item.maxItemCount = newFieldValue.length;
            item.max = newFieldValue.value;
            item.min = newFieldValue.value;
            item.isNew = false;
            if (item.fieldType == "N") {
                item.sum = parseFloat(newFieldValue.value);
            }
        } else {
            if (item.fieldType == "N") {
                item.sum += parseFloat(newFieldValue.value);
            }
            item.max = item.max == null ?
                newFieldValue.value
                : item.max < newFieldValue.value ? newFieldValue.value : item.max;
            item.min = item.min == null ?
                newFieldValue.value :
                item.min > newFieldValue.value ? newFieldValue.value : item.min;
            item.minItemCount = item.minItemCount == null ?
                newFieldValue.length :
                item.minItemCount > newFieldValue.length ? newFieldValue.length : item.minItemCount;
            item.maxItemCount = item.maxItemCount == null ?
                newFieldValue.length :
                item.maxItemCount < newFieldValue.length ? newFieldValue.length : item.maxItemCount;
        }
    }
};

const getFieldValueAndType = (data, fieldName, fieldPath) => {
    let returnValue = null;

    if (data == null || data[fieldName] == null) {
        return null;
    }
    let currentObject = data;
    for (let i = 0; i < fieldPath.length; i++) {
        const obj = currentObject[fieldPath[i]];
        if (obj == null) {
            continue;
        }
        let basicTypes = ["S", "N", "BOOL", "SS", "NS"];
        basicTypes.forEach(type => {
            if (obj[type] != null) {
                returnValue = {type: type, value: obj[type], length: 1};
                if (type == "NS" || type == "SS" && Array.isArray(obj[type])) {
                    returnValue.length = obj[type].length;
                    returnValue.value = obj[type].join(",");
                }
            }
        });

        if (obj.M != null) {
            currentObject = obj.M;
            continue;
        }
    }

    return returnValue;
};

exports.fillTables = fillTables;