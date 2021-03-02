CREATE TABLE IF NOT EXISTS `${tableName}` (
  `Hash` VARCHAR(64) NOT NULL,
  `StartDate` DATE,
  `EndDate` DATE,
  `GroupValue` NVARCHAR(100) NOT NULL,
  `FieldName` NVARCHAR(100) NOT NULL,
  `FieldValue` NVARCHAR(100) NULL,
  `InsertCount` INT NULL,
  `UpdateCount` INT NULL,
  `DeleteCount` INT NULL,
  `MinItemsCount` INT NULL,
  `MaxItemsCount` INT NULL,
  `Max` DOUBLE NULL,
  `MIN` DOUBLE NULL,
  `SUM` DOUBLE NULL,

  PRIMARY KEY (`Hash`,`StartDate`,`EndDate`));
