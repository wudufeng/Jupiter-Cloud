package com.jupiter.cloud.upms.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.jupiterframework.model.GenericPo;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;


/**
 * 租户信息
 *
 * @author jupiter
 * @since 2019-08-07
 */
@lombok.Data
@lombok.EqualsAndHashCode(callSuper = false)
@TableName("sys_tenant")
@Schema
public class Tenant extends GenericPo {

    private static final long serialVersionUID = 1L;

    /** 租户编码 */
    @TableId(value = "tenant_id", type = IdType.AUTO)
    private Long tenantId;

    /** 租户名称 */
    private String name;

    /** 租期开始时间 */
    @TableField("begin_time")
    private Date beginTime;

    /** 租期结束时间 */
    @TableField("end_time")
    private Date endTime;

    /** 状态：1正常 2冻结 3注销 */
    private Integer status;

    /** 联系人 */
    @TableField("contact_user")
    private String contactUser;

    /** 联系电话 */
    @TableField("contact_phone")
    private String contactPhone;

    /** 联系邮箱 */
    @TableField("contact_email")
    private String contactEmail;

    /** 绑定域名 */
    @TableField("domain")
    private String domain;
}
