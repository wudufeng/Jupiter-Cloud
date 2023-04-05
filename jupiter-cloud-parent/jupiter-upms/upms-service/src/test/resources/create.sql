drop table if exists sys_tenant;
create table sys_tenant(
	tenant_id int(11) not null auto_increment comment '租户编码',
	name  varchar(32) not null comment '租户名称',
	begin_time date not null comment '租期开始时间',
	end_time date not null comment '租期结束时间',
	status int not null default 1 comment '状态:1-正常,2-冻结,3-注销',
	contact_user  varchar(64) not null comment '联系人' ,
	contact_phone varchar(64) not null comment '联系电话' ,
	contact_email varchar(64) not null comment '联系邮箱' ,
	domain varchar(128) not null default '' comment '绑定域名' ,
	create_time timestamp not null default current_timestamp comment '创建时间',
	update_time timestamp not null default current_timestamp on update current_timestamp comment '最后更新时间',
	primary key (tenant_id)
) comment '租户信息';

drop table if exists sys_organization;
create table sys_organization (
	id int(11) not null auto_increment comment '机构id',
	tenant_id int(11) not null comment '租户编码',
	code varchar(18) not null comment '机构编码，从第一层开始拼接，同级机构使用16进制两位数递增',
	level int(2) not null comment '机构层级',
	name varchar(50) not null comment '机构名称',
	full_name varchar(250) not null default '' comment '机构全名称',
	parent_code varchar(18) not null default '' comment '上级机构编号',
	sort int(2) default 1 null comment '排序',
	type int(1) default 1 not null comment '机构类型:1-公司,2-部门',
	is_del tinyint(1) not null default 0 comment '是否删除: 0-正常,1-已删除',
	create_time timestamp not null default current_timestamp comment '创建时间',
	update_time timestamp not null default current_timestamp on update current_timestamp comment '最后更新时间',
  primary key (id),
  unique key uk_organization_1(tenant_id,code) using hash,
  key idx_organization_1(parent_code) using hash
) comment='组织架构';

drop table if exists sys_post;
create table sys_post (
	id int(11) not null auto_increment comment '岗位编码',
	tenant_id int(11) not null comment '租户编码',
	name varchar(50) not null comment '岗位名称',
	description varchar(250) not null default '' comment '岗位描述',
	sort int(2) default 1 null comment '排序',
	type int(1) default 1 not null comment '岗位类型:1-管理岗,2-技术岗,3-高层,4-基层',
	is_del tinyint(1) not null default 0 comment '是否删除: 0-正常,1-已删除',
	create_time timestamp not null default current_timestamp comment '创建时间',
	update_time timestamp not null default current_timestamp on update current_timestamp comment '最后更新时间',
  primary key (id),
  key idx_post_1(tenant_id) using hash
) comment='岗位信息';


drop table if exists sys_application;
create table sys_post (
	id int(11) not null auto_increment comment '岗位编码',
	tenant_id int(11) not null comment '租户编码',
	name varchar(50) not null comment '岗位名称',
	description varchar(250) not null default '' comment '岗位描述',
	sort int(2) default 1 null comment '排序',
	type int(1) default 1 not null comment '岗位类型:1-管理岗,2-技术岗,3-高层,4-基层',
	is_del tinyint(1) not null default 0 comment '是否删除: 0-正常,1-已删除',
	create_time timestamp not null default current_timestamp comment '创建时间',
	update_time timestamp not null default current_timestamp on update current_timestamp comment '最后更新时间',
  primary key (id),
  key idx_post_1(tenant_id) using hash
) comment='岗位信息';



drop table if exists sys_menu;
create table sys_menu (
  id int(11) not null auto_increment comment '菜单id',
  code varchar(18) not null comment '菜单编码，从第一层开始拼接，同级使用16进制两位数递增',
  parent_code varchar(18) not null default '' comment '上级菜单编号',
  name varchar(32) not null comment '菜单名称',
  path varchar(128) not null default '' comment '前端url',
  icon varchar(32) not null default '' comment '图标',
  component varchar(128) not null default '' null comment 'vue页面',
  sort int(11) not null default 1 comment '排序值',
  keep_alive char(1) not null default '0' comment '路由缓存:0-开启,1-关闭',
  menu_type char(1) not null comment '菜单类型:0-菜单,1-按钮）',
  permission varchar(32) default null comment '菜单权限标识',
  is_del tinyint(1) not null default 0 comment '是否删除: 0-正常,1-已删除',
  create_time timestamp not null default current_timestamp comment '创建时间',
  update_time timestamp not null default current_timestamp on update current_timestamp comment '最后更新时间',
  primary key (id),
  unique key uk_menu_1(code),
  key idx_menu_1(parent_code)
) comment='菜单管理';


drop table if exists sys_role;
create table sys_role (
  id int(11) not null auto_increment comment '角色id',
  tenant_id int(11) not null comment '租户编码',
  name varchar(64) not null comment '角色名称',
  code varchar(64) not null comment '角色标识',
  description varchar(255) not null comment '角色描述',
  data_type tinyint(1) not null comment '数据权限:1-全部,2-自定义,3-本级及子级,4-本级',
  create_time timestamp not null default current_timestamp comment '创建时间',
  update_time timestamp not null default current_timestamp on update current_timestamp comment '最后更新时间',
  is_del tinyint(1) not null default 0 comment '是否删除:0-正常,1-已删除',
  primary key (id),
  unique key uk_role_1(code)
) comment='角色管理';

drop table if exists sys_data_relative;
create table sys_data_relative (
  id int(11) not null auto_increment comment '主键id', -- 无意义并且不会被关联的id，使用自增
  scope_id bigint(20) not null comment '范围id',
  instance_id bigint(20) not null comment '实例id',
  ref_type int(2) not null comment '关联类型:1-角色菜单,2-角色自定义数据权限,3-机构角色,4-用户角色',
  create_time timestamp not null default current_timestamp comment '创建时间',
  update_time timestamp not null default current_timestamp on update current_timestamp comment '最后更新时间',
  is_enable tinyint(1) not null default 1 comment '是否启用:1-启用,0-禁用',
  primary key (id),
  unique key uk_data_relative_1(scope_id,instance_id,ref_type) using hash
) comment='一对多关系映射表';


drop table if exists sys_employee;
create table sys_employee (
  id int(11) not null auto_increment comment '主键id', -- 无意义并且不会被关联的id，使用自增
  tenant_id int(11) not null comment '租户编码',
  organization_id int(11) not null comment '机构',
  user_id bigint(20) not null comment '用户id',
  post_id int(11) not null comment '岗位',
  job_num varchar(8) not null comment '工号',
  enter_time date not null comment '入职日期',
  leave_time date null comment '离职日期',
  status char(1) not null default '1' comment '状态:1-在职,2-离职',
  create_time timestamp not null default current_timestamp comment '创建时间',
  update_time timestamp not null default current_timestamp on update current_timestamp comment '修改时间',
  primary key (id),
  unique key uk_employee_1 (organization_id,user_id) using hash,
  key idx_employee_1(user_id) using hash,
  key idx_employee_2(job_num) using hash
) comment='员工管理';



drop table if exists user_info;
create table user_info (
  user_id bigint(20) not null comment '主键id',
  nickname varchar(64) not null default '' comment '昵称',
  realname varchar(64) not null default '' comment '姓名',
  sex char(1) not null default '0' comment '性别:0-未知,1-男,2-女',
  head_img_url varchar(255) not null default '' comment '头像url',
  phone varchar(18) not null default '' comment '手机号码',
  email varchar(64) not null default '' comment '邮箱',
  birthday date null comment '出生日期',
  id_type int(1) not null default 1 comment '证件类型:1-身份证',
  id_code varchar(64) not null default '' comment '证件号码',
  country varchar(64) not null default '' comment '所在国家',
  province varchar(64) not null default '' comment '所在省份',
  city varchar(64) not null default '' comment '所在城市',
  address varchar(255) not null default '' comment '住址',
  signature varchar(255) not null default '' comment '个性签名',
  src_type int(2) not null default 1 comment '用户来源',
  status char(1) not null default '1' comment '状态:1-正常,2-锁定,3-注销',
  create_time timestamp not null default current_timestamp comment '创建时间',
  update_time timestamp not null default current_timestamp on update current_timestamp comment '修改时间',
  primary key (user_id)
) charset=utf8mb4 collate=utf8mb4_bin comment='用户信息';

drop table if exists user_login;
create table user_login (
  user_id bigint(20) not null comment '主键id',
  login_name varchar(64) not null comment '用户名',
  password varchar(128) not null comment '登录密码',
  phone_login_flag tinyint(1) not null comment '是否允许手机号码登录:0-否,1-是',
  email_login_flag tinyint(1) not null comment '是否允许email登录:0-否,1-是',
  pwd_reset tinyint(1) not null default 1 comment '是否需要重设密码:0-否,1-是',
  error_count int(1) not null default 1 comment '连续错误次数',
  first_login_time datetime null comment '首次登录时间',
  latest_login_time datetime null comment '最近登录时间',
  primary key (user_id),
  unique key uk_user_1 (login_name)
)  comment='用户登录信息';
