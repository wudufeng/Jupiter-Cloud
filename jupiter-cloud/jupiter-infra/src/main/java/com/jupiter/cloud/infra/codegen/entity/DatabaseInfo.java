package com.jupiter.cloud.infra.codegen.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.jupiterframework.model.GenericPo;


/**
 * 数据源信息
 *
 * @author jupiter
 * @since 2019-08-02
 */
@lombok.Data
@lombok.EqualsAndHashCode(callSuper = true)
@TableName("codegen_database_info")
public class DatabaseInfo extends GenericPo {

    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 租户编码 */
    @TableField("tenant_id")
    private String tenantId;

    /** 数据源名称 */
    @TableField("db_name")
    private String dbName;

    /** JdbcUrl */
    @TableField("jdbc_url")
    private String jdbcUrl;

    /** 用户名 */
    @TableField("user_name")
    private String userName;

    /** 密码 */
    private String password;

}
