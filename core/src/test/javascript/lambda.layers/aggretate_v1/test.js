require("jest");
const aggregate = require("../../../../main/resources/lambda/layers/aggretate_v1/nodejs/index.js");
const getRecords1 = () => [
    {
        "eventID": "2c6bcb4c6ab1e127fe179f9b6b48872b",
        "eventName": "INSERT",
        "eventVersion": "1.1",
        "eventSource": "aws:dynamodb",
        "awsRegion": "us-east-2",
        "dynamodb": {
            "ApproximateCreationDateTime": 1614246055,
            "Keys": {
                "author": {
                    "S": "12123"
                },
                "title": {
                    "S": "123123"
                }
            },
            "NewImage": {
                "number": {
                    "N": "12"
                },
                "Bool": {
                    "BOOL": true
                },
                "author": {
                    "S": "12123"
                },
                "StringSet": {
                    "SS": [
                        "asd",
                        "asdasd"
                    ]
                },
                "name": {
                    "S": "name"
                },
                "category": {
                    "S": "a"
                },
                "title": {
                    "S": "123123"
                }
            },
            "SequenceNumber": "487403700000000004006594494",
            "SizeBytes": 94,
            "StreamViewType": "NEW_AND_OLD_IMAGES"
        },
        "eventSourceARN": "arn:aws:dynamodb:us-east-2:476844086115:table/musics/stream/2021-01-04T16:38:55.894"
    }
];
const getFieldRefences1 = () => {
    return {
        groups: [{
            tableName: "category",
            fieldName: "category",
            path: ["category"],
            fields: [{
                fieldName: "name",
                path: ["name"]
            }]
        }]
    }
};

describe("Aggregate_v1 testing", () => {

    test("check exported functions", () => {
        expect(aggregate.fillTables).not.toBeNull();
        expect(aggregate.buildScripts()).not.toBeNull();
    });

    test("test Basic Insert", () => {
        const fieldReferences = getFieldRefences1();

        var tables = {category: {}};
        var records = getRecords1();
        aggregate.fillTables(tables, fieldReferences, records, (value) => value);
        expect(tables.category.groups[0].items).not.toBeNull();
        expect(tables.category.groups[0].groupValue.value).toBe("a");
    });

    test("test Insert with three records", () => {
        const fieldReferences = getFieldRefences1();

        var tables = {category: {}};
        var records = getRecords1();
        records.push(JSON.parse(JSON.stringify(records[0])));
        records.push(JSON.parse(JSON.stringify(records[0])));
        records[1].dynamodb.NewImage.name.S = "x";
        aggregate.fillTables(tables, fieldReferences, records, (value) => value);
        let group = tables.category.groups[0];
        expect(group.items.length).toBe(1);
        let item = group.items[0];
        expect(item.insertCount).toBe(3);
        expect(item.updateCount).toBe(0);
        expect(item.updateCount).toBe(0);
        expect(item.max).toBe('x');
    });

    test("test Insert with three records with numeric field", () => {
        const fieldReferences = getFieldRefences1();

        var tables = {category: {}};
        var records = getRecords1();
        var recordValue = records[0].dynamodb.NewImage;
        delete recordValue.name.S;
        recordValue.name.N = 2;
        records.push(JSON.parse(JSON.stringify(records[0])));
        records.push(JSON.parse(JSON.stringify(records[0])));
        records[1].dynamodb.NewImage.name.N = "10";
        aggregate.fillTables(tables, fieldReferences, records, (value) => value);
        let group = tables.category.groups[0];
        expect(group.items.length).toBe(1);
        let item = group.items[0];
        expect(item.insertCount).toBe(3);
        expect(item.updateCount).toBe(0);
        expect(item.updateCount).toBe(0);
        expect(item.max).toBe('10');
        expect(item.sum).toBe(14);
    });
    test("test Basic Update", () => {
        const fieldReferences = getFieldRefences1();

        var tables = {category: {}};
        var records = getRecords1();
        records[0].eventName = "MODIFY";
        records[0].dynamodb.OldImage = records[0].dynamodb.NewImage;
        records[0].dynamodb.OldImage.name.S = "test";
        aggregate.fillTables(tables, fieldReferences, records, (value) => value);
        expect(tables.category.groups[0].items).not.toBeNull();
        expect(tables.category.groups[0].groupValue.value).toBe("a");
    });

    test("test Update - changing group value must generate two update records ", () => {
        const fieldReferences = getFieldRefences1();

        var tables = {category: {}};
        var records = getRecords1();
        records[0].eventName = "MODIFY";
        records[0].dynamodb.OldImage = JSON.parse(JSON.stringify(records[0].dynamodb.NewImage));
        records[0].dynamodb.OldImage.category.S = "test";
        aggregate.fillTables(tables, fieldReferences, records, (value) => value);

        var oldValue = tables.category.groups.filter((item) => item.groupValue.value == "test");
        var newValue = tables.category.groups.filter((item) => item.groupValue.value == "a");
        expect(oldValue.length).toBe(1);
        expect(newValue.length).toBe(1);
        expect(oldValue[0].items[0].deleteCount).toBe(1);
        expect(newValue[0].items[0].updateCount).toBe(1);
        expect(newValue[0].items[0].insertCount).toBe(0);
    });
});