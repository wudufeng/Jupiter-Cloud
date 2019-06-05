DROP TABLE IF EXISTS sequence_definition;
CREATE table sequence_definition(
	tenant_id  varchar(32) NOT NULL ,
	seq_name  varchar(32) NOT NULL ,
	min_value  decimal(19,0) NOT NULL  ,
	max_value  decimal(19,0) NOT NULL ,
	partition_min_value  decimal(19,0) NOT NULL ,
	partition_max_value  decimal(19,0) NOT NULL ,
	increase  int NOT NULL ,
	current_value  decimal(19,0) NOT NULL ,
	cycle  tinyint NOT NULL ,
	char_length  int NULL ,
	prefix  varchar(8) NULL ,
	append_date_format  varchar(17) NULL ,
	PRIMARY KEY (tenant_id, seq_name)
);
 