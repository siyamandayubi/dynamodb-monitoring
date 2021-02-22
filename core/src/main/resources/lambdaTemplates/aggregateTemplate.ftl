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

var token = await mysqlUtil.getPrivateKeyValue(client, secret_key);

const connectionConfig = {
    host: process.env.sql_endpoint,
    port: process.env.sql_port,
    user: token.userName,
    database:  process.env.sql_database,
    ssl: true,
    password: token.password
};


exports.handler = async function (event, context) {
    // list of tables
    var tables = {};
    <#list entity.groups as group>
        tables.${group.tableName} = tables.${group.tableName} || { groups: [] };
    </#list>

    event.Records.forEach(function (record) {
        <#list entity.groups as group>
            // ${group.tableName}
            if (record.${group.fieldName} != null) {
               var table = tables.${group.tableName};
               var groupValue = record.${group.fieldName}
               <#list group.fields as field>
                   var fieldName = "${field.name}";
                   var fieldValue = "";
                   if (record.${field.name} != null){
                       fieldValue = record.${field.name}
                   }
                   var hashSource = groupValue + ":" + fieldName + "=" + fieldValue;
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

        var current = new Date();
        var scripts = [];
        var scriptTemplate = "INSERT INTO @table (GroupValue,     FieldName,    FieldValue,     StartDate,   EndDate,    Count,   Max,    Min,   Hash)"
                             + "VALUES            ('@GroupValue', '@FieldName', '@FieldValue', '@StartDate', '@EndDate', @Count, '@Max', '@Min', '@Hash')"
                             + " ON DUPLICATE KEY UPDATE count = count + @Count, Min = IF(Min<'@Min',Min,'@Min'),  Max = IF(Max>'@Max',Max,'@Max')"
        for(var tableName in tables){
            if(!tables.hasOwnProperty(tableName)){
                continue;
            }

            var table = tables[tableName];
            for(var groupValueName in table){
                if (!table.hasOwnProperty(groupValueName)){
                    continue;
                }

                var groupValue = table[groupValueName];
                for(var hash in groupValue){
                    if (!groupValue.hasOwnProperty(hash)){
                        continue;
                    }
                    var field = groupValue[hash];
                    var script = scriptTemplate.replace("@table", tableName);
                    script = script
                            .replaceAll("@Hash", hash)
                            .replaceAll("@GroupValue", groupValue.groupValue)
                            .replaceAll("@FieldName", field.fieldName)
                            .replaceAll("@FieldValue", field.fieldValue)
                            .replaceAll("@Count", field.count)
                            .replaceAll("@Min", field.min)
                            .replaceAll("@Max", field.max)
                            .replaceAll("@EndDate", current.toLocaleString())
                            .replaceAll("@StartDate", current.toLocaleString());

                    scripts.push(script);
                }
            }
        }

        if(scripts.length > 0){
            var scriptsStr = scripts.join(';');
            await mysqlUtil.executeSql(connectionConfig, scriptsStr,[]);
        }
    });
}

