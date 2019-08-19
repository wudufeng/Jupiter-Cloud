drop table if exists sys_tenant;
create table sys_tenant(
	tenant_id bigint(20) not null comment '租户编码',
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

drop table if exists sys_organization;
CREATE TABLE sys_organization (
	id bigint(20) NOT NULL comment '机构ID',
	tenant_id bigint(20) not null comment '租户编码',
	code varchar(18) not null comment '机构编码，从第一层开始拼接，同级机构使用16进制两位数递增',
	level int(2) not null comment '机构层级',
	name varchar(50) not NULL COMMENT '机构名称',
	parent_code varchar(18) not NULL default '' comment '上级机构编号',
	sort int(2) DEFAULT 1 NULL COMMENT '排序',
	is_del tinyint(1) not null DEFAULT 0 COMMENT '是否删除 0：正常, 1：已删除',
	create_time timestamp not null default current_timestamp comment '创建时间',
	update_time timestamp not null default current_timestamp on update current_timestamp comment '最后更新时间',
  PRIMARY KEY (id),
  unique key uk_organization_1(tenant_id,code)
) COMMENT='组织架构';

DROP TABLE IF EXISTS sys_user_info;
CREATE TABLE user_info (
  user_id bigint(20) NOT NULL COMMENT '主键ID',
  username varchar(64) NOT NULL COMMENT '用户名',
  password varchar(128) NOT NULL COMMENT '登录密码',
  nickname varchar(64) not null DEFAULT '' COMMENT '昵称',
  realname varchar(64) not null DEFAULT '' COMMENT '姓名',
  sex char(1) not null default '0' COMMENT '性别：0-未知 1-男 2-女',
  head_img_url varchar(255) not null default '' COMMENT '头像url',
  phone varchar(18) not null DEFAULT '' COMMENT '手机号码',
  email varchar(64) not null DEFAULT '' COMMENT 'Email',
  country varchar(64) not null DEFAULT '' COMMENT '所在国家',
  province varchar(64) not null DEFAULT '' COMMENT '所在省份',
  city varchar(64) not null DEFAULT '' COMMENT '所在城市',
  address varchar(255) not null DEFAULT '' COMMENT '住址',
  signature varchar(255) not null DEFAULT '' COMMENT '个性签名',
  status char(1) not null DEFAULT '1' COMMENT '状态：1-正常，2-锁定,3-注销',
  create_time timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  update_time timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (user_id),
  UNIQUE KEY uk_user_info_1 (username)
) CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='用户信息';
