const mysqlUtil = require("/opt/nodejs/mysql")
const region = process.env.AWS_REGION;
var AWS = require('aws-sdk');

var crypto = require('crypto');
var hash = crypto.createHash('sha256');

// Create a Secrets Manager client
var client = new AWS.SecretsManager({
    region: region
});

const secret_key = process.env.secret_key;

var token = await _getPrivateKeyValue(secret_key);

const connectionConfig = {
    host: process.env.sql_endpoint,
    port: process.env.sql_port,
    user: token.username,
    database:  process.env.sql_database,
    ssl: true,
    password: token.password
};


exports.handler = async function (event, context) {
    // list of tables
    var tables = {};
    <#list entity.groups as group>
        tables.${group.tableName} = tables.${group.tableName} | {};
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
                    var hashValue = hash.update(hashSource).digest(hashSource)
                   table.groupValue = table.groupValue | {groupValue: groupValue};
                   if (table.groupValue[hashValue] == null){
                        table.groupValue[hashValue] = {fieldName: fieldName, fieldValue: fieldValue, count:1, max: fieldValue, min: fieldValue}
                   }
                   else{
                       table.groupValue[hashValue].count++;
                       table.groupValue[hashValue].max = table.groupValue[hashValue].max < fieldValue? fieldValue : table.groupValue[hashValue].max;
                       table.groupValue[hashValue].min = table.groupValue[hashValue].min > fieldValue? fieldValue : table.groupValue[hashValue].min;
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
                    var script = scriptTemplate.replace(tableName);
                    script = script
                            .replaceAll("@GroupValue", groupValueName)
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
            var scriptsStr = sripts.join(';');
           _executeSql(connectionConfig, scriptsStr,[]);
        }
    });
}

