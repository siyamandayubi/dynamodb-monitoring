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
    <#list entity.fields as field>
        tables.${field.tableName} = tables.${fieldtableName} | {};
    </#list>

    event.Records.forEach(function (record) {
        <#list entity.fields as field>
            // ${field.tableName}
            var table = tables.${field.tableName};
            if (record.${field.path} != null) {
               var hashValue = hash.update(code).digest(code)
            }
        tables.${field.tableName} = tables.${fieldtableName} | {};
        console.log(record.eventID);
        console.log(record.eventName);
        console.log('DynamoDB Record: %j', record.dynamodb);
        </#list>
    });
}