drop table if exists sys_tenant;
create table sys_tenant(
	tenant_id varchar(32) not null comment '租户编码',
	name  varchar(32) not null comment '租户名称',
	begin_time date not null comment '租期开始时间',
	end_time date not null comment '租期结束时间',
	status int not null comment '状态：1正常 2冻结 3注销',
	contact_user  varchar(64) not null comment '联系人' ,
	contact_phone varchar(64) not null comment '联系电话' ,
	contact_email varchar(64) not null comment '联系邮箱' ,
	create_time timestamp not null default current_timestamp comment '创建时间',
	update_time timestamp not null default current_timestamp on update current_timestamp comment '最后更新时间',
	primary key (tenant_id)
) comment '租户信息';
 