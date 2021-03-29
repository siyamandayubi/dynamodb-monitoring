const mysqlUtil = require("/opt/nodejs/mysql")
const aggregateUtil = require("/opt/nodejs/aggregate_v1")
const region = process.env.AWS_REGION;
var AWS = require('aws-sdk');

var crypto = require('crypto');

// Create a Secrets Manager client
var client = new AWS.SecretsManager({
    region: region
});

const secret_key = process.env.secret_key;

var token = null;

exports.handler = async function (event, context) {
    if (token == null) {
        token = await mysqlUtil.getPrivateKeyValue(client, secret_key);
    }

    // list of tables
    var tables = {};
    var fieldReferences = { groups: []};
    var group, field;
    <#list entity.groups as group>
        tables.${group.tableName} = tables.${group.tableName} || { groups: [] };
        <#assign pathList = group.path?map(p -> "'" + p + "'") >
        <#assign path = pathList?join(", ") >
        group = { fieldName: "${group.fieldName}", path:[${path}], tableName:"${group.tableName}", fields:[]};
        fieldReferences.groups.push(group);
        <#list group.fields as field>
            <#assign pathList = field.path?map(p -> "'" + p + "'") >
            <#assign path = pathList?join(", ") >
            field = {fieldName: "${field.name}", path:[${path}] };
            group.fields.push(field);
        </#list>
    </#list>

    aggregateUtil.fillTables(tables, fieldReferences, event.Records,(v)=>{return crypto.createHash('sha256').update(v).digest("hex");});
    var scripts = buildScripts(tables)

    if(scripts.length > 0){
       for(var i = 0; i < scripts.length; i++)
       {
            var script = scripts[i];
            var hash = script.hash.substring(0,3);
            var endpoint = dbConfigs.endpoints.filter(item => item.start <= hash && item.end >= hash);
            if (endpoint.length > 0){
                connectionConfig = {
                    host: endpoint[0].endPoint,
                    port: endpoint[0].port,
                    user: token.username,
                    database: dbConfigs.databaseName,
                    ssl: true,
                    password: token.password
                };
                await mysqlUtil.executeSql(connectionConfig, script.script);
            }
       };
    }
}

const buildScripts = (tables) => {
    let scripts = [];
    const scriptTemplate = "INSERT INTO @table (GroupValue,     FieldName,    FieldValue,     StartDate,   EndDate,    InsertCount, UpdateCount, DeleteCount,   Max,    Min, MinItemsCount, MaxItemsCount, Sum,   Hash)" +
    "VALUES            ('@GroupValue', '@FieldName', '@FieldValue', '@StartDate', '@EndDate', @InsertCount, @UpdateCount, @DeleteCount, '@Max', '@Min', @MinItemCount, @MaxItemCount, @Sum, '@Hash')" +
    " ON DUPLICATE KEY UPDATE InsertCount = InsertCount + @InsertCount,  UpdateCount = UpdateCount + @UpdateCount,  DeleteCount = DeleteCount + @DeleteCount, Min = IF(Min<'@Min',Min,'@Min'),  Max = IF(Max>'@Max',Max,'@Max')";
    const date = getDate(new Date());
    for (const tableName in tables) {
        if (!tables.hasOwnProperty(tableName)) {
             continue;
        }

        const table = tables[tableName];
        table.groups.forEach(groupValue => {
            groupValue.items.forEach((item) => {
                let script = scriptTemplate.replace("@table", tableName);
                script = script
                    .replaceAll("@Hash", item.hash)
                    .replaceAll("@GroupValue", groupValue.groupValue.value)
                    .replaceAll("@FieldName", item.fieldName)
                    .replaceAll("@FieldValue", item.fieldValue)
                    .replaceAll("@InsertCount", item.insertCount)
                    .replaceAll("@UpdateCount", item.updateCount)
                    .replaceAll("@DeleteCount", item.deleteCount)
                    .replaceAll("@Sum", item.sum)
                    .replaceAll("@MinItemCount", item.minItemCount)
                    .replaceAll("@MaxItemCount", item.maxItemCount)
                    .replaceAll("@Min", item.min)
                    .replaceAll("@Max", item.max)
                    .replaceAll("@EndDate", date)
                    .replaceAll("@StartDate", date);

                scripts.push({script: script, hash: item.hash});
            })
        });
    }

    return scripts;
}

const getDate = (date) => {
    return date.getFullYear().toString() + "-" + (date.getMonth() + 1).toString() + "-" + date.getDate().toString();
}

const dbConfigs = ${dbConfig};