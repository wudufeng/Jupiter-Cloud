package com.jupiter.codegen.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.jupiterframework.model.GenericPo;

import io.swagger.annotations.ApiModelProperty;


/**
 * 数据源信息
 *
 * @author WUDUFENG
 * @since 2019-08-02
 */
@lombok.Data
@lombok.EqualsAndHashCode(callSuper = true)
@TableName("codegen_database_info")
public class DatabaseInfo extends GenericPo {

    private static final long serialVersionUID = 1L;

    /** 主键 */
    @ApiModelProperty(value = "主键")
    private Long id;

    /** 租户编码 */
    @ApiModelProperty(value = "租户编码")
    @TableField("tenant_id")
    private String tenantId;

    /** 数据源名称 */
    @ApiModelProperty(value = "数据源名称")
    @TableField("db_name")
    private String dbName;

    /** JdbcUrl */
    @ApiModelProperty(value = "JdbcUrl")
    @TableField("jdbc_url")
    private String jdbcUrl;

    /** 用户名 */
    @ApiModelProperty(value = "用户名")
    @TableField("user_name")
    private String userName;

    /** 密码 */
    @ApiModelProperty(value = "密码")
    private String password;


}
