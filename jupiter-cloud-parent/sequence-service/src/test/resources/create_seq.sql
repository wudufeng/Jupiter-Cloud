drop table if exists sequence_definition;
create table sequence_definition(
	id bigint(19) not null comment '主键',
	tenant_id  varchar(32) not null comment '租户编码',
	seq_name  varchar(32) not null comment '序列名称',
	min_value  bigint(19) not null  comment '最小值',
	max_value  bigint(19) not null comment '最大值',
	increase  int not null comment '增长缓冲区',
	current_value  bigint(19 ) not null comment '当前值' ,
	cycle tinyint(1) not null comment '是否允许循环 0否，1是' ,
	char_length int not null comment '字符长度，不足位左补0',
	prefix  varchar(8) not null default '' comment '序列前缀',
	append_date_format  varchar(17) not null default '' comment '日期填充格式',
	create_time timestamp not null default current_timestamp comment '创建时间',
	update_time timestamp not null default current_timestamp on update current_timestamp comment '最后更新时间',
	primary key (id),
	unique key tenant_seq(tenant_id, seq_name)
) comment '序列定义';
 