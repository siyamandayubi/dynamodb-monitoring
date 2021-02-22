const mysql = require('mysql'); //https://www.npmjs.com/package/mysql2
const util = require('util');
const AWS = require('aws-sdk');

const getPrivateKeyValue = async function (client, secret_key) {

    return new Promise((resolve, reject) => {
        client.getSecretValue({SecretId: secret_key}, function (err, data) {
            if (err) {
                reject(err);
            } else {
                if ('SecretString' in data) {
                    resolve(JSON.parse(data.SecretString));
                } else {
                    let buff = new Buffer(data.SecretBinary, 'base64');
                    resolve(JSON.parse(buff.toString('ascii')));
                }
            }
        });
    });
};


const executeSql = async function (connectionConfig, sql, values) {
    const promise = new Promise(function (resolve, reject) {

        var connection = mysql.createConnection(connectionConfig);
        connection.on('error', function (err) {
            console.log("Error happend");
            console.log(err.code);
            console.log(err);
        });

        connection.connect(function (err) {
            if (err) {
                console.log('error connecting: ' + err.stack);
                return;
            }
        });

        connection.query(sql, values, function (error, results, fields) {
            if (error) {
                //throw error;
                console.log(error);
                reject("ERROR " + error);
            }


            connection.end(function (error, results) {
                if (error) {
                    //return "error";
                    reject("ERROR");
                }

                resolve(results);
            });
        });


    });

    return promise;
}

const executeSqls = async function (connectionConfig, sqls) {
    if (sqls == null || sqls.length == 0){
        return;
    }

    const conn = mysql.createConnection(connectionConfig);

    // node native promisify
    const query = util.promisify(conn.query).bind(conn);
    for(const sql of sqls ){
        await query(sql);
    }
}

exports.getPrivateKeyValue = getPrivateKeyValue;
exports.executeSql = executeSql
exports.executeSqls = executeSqls