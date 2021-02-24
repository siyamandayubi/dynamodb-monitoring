CREATE TABLE IF NOT EXISTS `${tableName}` (
  `Hash` VARCHAR(64) NOT NULL,
  `StartDate` DATE,
  `EndDate` DATE,
  `GroupValue` NVARCHAR(100) NOT NULL,
  `FieldName` NVARCHAR(100) NOT NULL,
  `FieldValue` NVARCHAR(100) NULL,
  `Count` INT NULL,
  `Max` NVARCHAR(100) NULL,
  `MIN` NVARCHAR(100) NULL,
  `SUM` DOUBLE NULL,

  PRIMARY KEY (`Hash`,`StartDate`,`EndDate`));
