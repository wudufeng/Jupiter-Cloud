drop table if exists codegen_database_info;
create table codegen_database_info(
	id bigint(19) not null comment '主键',
	tenant_id  varchar(32) not null comment '租户编码',
	db_name  varchar(32) not null comment '数据源名称',
	jdbc_url  varchar(12832) not null comment 'JdbcUrl',
	user_name  varchar(32) not null comment '用户名',
	password  varchar(32) not null comment '密码',
	create_time timestamp not null default current_timestamp comment '创建时间',
	update_time timestamp not null default current_timestamp on update current_timestamp comment '最后更新时间',
	primary key (id),
	unique key tenant_name(tenant_id, db_name)
) comment '数据源信息';


create table code_generator(
	table_name  varchar(32) not null comment '租户编码',
	table_comment  varchar(32) not null comment '表注释',
	table_index  varchar(32) not null comment '索引',
	table_create_time timestamp not null default current_timestamp comment '创建时间',
	package_name  varchar(32) not null comment '包名',
	author varchar(32) not null comment '作者',
	module_name varchar(32) not null comment '模块名称',
	table_prefix  varchar(32) not null comment '索引'
) comment '代码生成';
