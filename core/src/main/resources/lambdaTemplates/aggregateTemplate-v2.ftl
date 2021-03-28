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
        group = { fieldName: "${group.fieldName}", path:[${group.path}], tableName:"${group.tableName}", fields:[]};
        fieldReferences.groups.push(group);
        <#list group.fields as field>
            field = {fieldName: "${field.name}", path:[${field.path}] };
            group.fields.push(field);
        </#list>
    </#list>

    aggregateUtil.fillTables(tables, fieldReferences, event.Records,(v)=>{return crypto.createHash('sha256').update(v).digest("hex");});
    var scripts = aggregateUtil.buildScripts(tables)

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

const dbConfigs = ${dbConfig};