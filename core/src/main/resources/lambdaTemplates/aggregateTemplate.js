String.prototype.replaceAll = function(search, replace){
    return this.split(search).join(replace);
}

const mysqlUtil = require("/opt/nodejs/mysql")
const region = process.env.AWS_REGION;
var AWS = require('aws-sdk');

var crypto = require('crypto');

// Create a Secrets Manager client
var client = new AWS.SecretsManager({
    region: region
});

const secret_key = process.env.secret_key;

var connectionConfig = null;

exports.handler = async function (event, context) {
     if (connectionConfig == null) {
         var token = await mysqlUtil.getPrivateKeyValue(client, secret_key);
         connectionConfig = {
             host: process.env.sql_endpoint,
             port: process.env.sql_port,
             user: token.username,
             database: process.env.sql_database,
             ssl: true,
             password: token.password
         };
     }

    // list of tables
    var tables = {};
    <#list entity.groups as group>
        tables.${group.tableName} = tables.${group.tableName} || { groups: [] };
    </#list>

    event.Records.forEach(function (record) {
        if (record.eventName == "INSERT") {
            var newData = record.dynamodb.NewImage;
            <#list entity.groups as group>
                // ${group.tableName}
                if (newData.${group.fieldName} != null) {
                   var table = tables.${group.tableName};
                   var groupValue = newData.${group.fieldName}.S;
                   <#list group.fields as field>
                       var fieldName = "${field.name}";
                       var fieldValue = "";
                       if (newData.${field.name} != null){
                           fieldValue = newData.${field.name}.S;
                       }
                       var hashSource = groupValue + ":" + fieldName;
                        var hashValue = crypto.createHash('sha256').update(hashSource).digest("hex")
                       var existingGroup = table.groups.filter((g)=> { g.groupValue == groupValue });

                       var groupValueObj = existingGroup.length > 0 ? existingGroup[0] : { items: [] };
                       if (existingGroup.length == 0) {
                           table.groups.push(groupValueObj);
                       }

                       groupValueObj.groupValue = groupValue;
                       var existingItems = groupValueObj.items.filter(function() { this.hash == hashValue });
                       if (existingItems.length == 0) {
                           groupValueObj.items.push({ hash: hashValue, fieldName: fieldName, fieldValue: fieldValue, count: 1, max: fieldValue, min: fieldValue })
                       }
                       else {
                          var item = existingItems[0];
                          item.count++;
                          item.max = item.max < fieldValue ? fieldValue : item.max;
                          item.min = item.min > fieldValue ? fieldValue : item.min;
                       }

                   </#list>
                }
            </#list>
         }
    });

    var scripts = buildScripts(tables);
    if(scripts.length > 0){
       await mysqlUtil.executeSqls(connectionConfig, scripts);
    }
}

const getDate = (date) => {
   return date.getFullYear().toString() + "-" + (date.getMonth() + 1).toString() + "-" + date.getDate().toString();
}

const buildScripts = (tables) => {
    var scripts = [];
    var scriptTemplate = "INSERT INTO @table (GroupValue,     FieldName,    FieldValue,     StartDate,   EndDate,    Count,   Max,    Min,   Hash)" +
        "VALUES            ('@GroupValue', '@FieldName', '@FieldValue', '@StartDate', '@EndDate', @Count, '@Max', '@Min', '@Hash')" +
        " ON DUPLICATE KEY UPDATE count = count + @Count, Min = IF(Min<'@Min',Min,'@Min'),  Max = IF(Max>'@Max',Max,'@Max')"
    console.log(JSON.stringify(tables));
    var date = getDate(new Date());
    for (var tableName in tables) {
        if (!tables.hasOwnProperty(tableName)) {
            continue;
        }

        var table = tables[tableName];
        table.groups.forEach(groupValue => {
            groupValue.items.forEach((item) => {
                var script = scriptTemplate.replace("@table", tableName);
                script = script
                    .replaceAll("@Hash", item.hash)
                    .replaceAll("@GroupValue", groupValue.groupValue)
                    .replaceAll("@FieldName", item.fieldName)
                    .replaceAll("@FieldValue", item.fieldValue)
                    .replaceAll("@Count", item.count)
                    .replaceAll("@Min", item.min)
                    .replaceAll("@Max", item.max)
                    .replaceAll("@EndDate", date)
                    .replaceAll("@StartDate", date);

                scripts.push(script);
            })
        });
    }

    return scripts;
}
