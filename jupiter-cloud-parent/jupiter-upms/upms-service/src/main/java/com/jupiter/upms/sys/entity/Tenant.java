package com.jupiter.upms.sys.entity;

import java.util.Date;

import org.apache.ibatis.type.JdbcType;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.jupiterframework.model.GenericPo;

import io.swagger.annotations.ApiModelProperty;


/**
 * 租户信息
 *
 * @author WUDUFENG
 * @since 2019-08-07
 */
@lombok.Data
@lombok.EqualsAndHashCode(callSuper = false)
@TableName("sys_tenant")
public class Tenant extends GenericPo {

    private static final long serialVersionUID = 1L;

    /** 租户编码 */
    @ApiModelProperty(value = "租户编码")
    @TableId(value = "tenant_id", type = IdType.AUTO)
    @TableField(jdbcType = JdbcType.INTEGER)
    private Long tenantId;

    /** 租户名称 */
    @ApiModelProperty(value = "租户名称")
    private String name;

    /** 租期开始时间 */
    @ApiModelProperty(value = "租期开始时间")
    @TableField("begin_time")
    private Date beginTime;

    /** 租期结束时间 */
    @ApiModelProperty(value = "租期结束时间")
    @TableField("end_time")
    private Date endTime;

    /** 状态：1正常 2冻结 3注销 */
    @ApiModelProperty(value = "状态：1正常 2冻结 3注销")
    private Integer status;

    /** 联系人 */
    @ApiModelProperty(value = "联系人")
    @TableField("contact_user")
    private String contactUser;

    /** 联系电话 */
    @ApiModelProperty(value = "联系电话")
    @TableField("contact_phone")
    private String contactPhone;

    /** 联系邮箱 */
    @ApiModelProperty(value = "联系邮箱")
    @TableField("contact_email")
    private String contactEmail;

    /** 绑定域名 */
    @ApiModelProperty(value = "绑定域名")
    @TableField("domain")
    private String domain;
}
