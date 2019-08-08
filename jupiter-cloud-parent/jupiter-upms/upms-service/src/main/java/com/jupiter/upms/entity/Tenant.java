package com.jupiter.upms.entity;

import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.jupiterframework.model.GenericPo;

import io.swagger.annotations.ApiModelProperty;


/**
 * 租户信息
 *
 * @author WUDUFENG
 * @since 2019-08-07
 */
@lombok.Data
@lombok.EqualsAndHashCode(callSuper = true)
@TableName("sys_tenant")
public class Tenant extends GenericPo {

    private static final long serialVersionUID = 1L;

    /** 租户编码 */
    @ApiModelProperty(value = "租户编码")
    @TableId("tenant_id")
    private String tenantId;

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


}
