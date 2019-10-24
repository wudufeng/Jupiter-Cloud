package com.jupiter.upms.sys.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.jupiterframework.model.GenericPo;

import io.swagger.annotations.ApiModelProperty;


/**
 * 角色管理
 *
 * @author WUDUFENG
 * @since 2019-10-25
 */
@lombok.Data
@lombok.EqualsAndHashCode(callSuper = true)
@TableName("sys_role")
public class Role extends GenericPo {

    private static final long serialVersionUID = 1L;

    /** 角色ID */
    @ApiModelProperty(value = "角色ID")
    private Long id;

    /** 租户编码 */
    @ApiModelProperty(value = "租户编码")
    @TableField("tenant_id")
    private Long tenantId;

    /** 角色名称 */
    @ApiModelProperty(value = "角色名称")
    private String name;

    /** 角色标识 */
    @ApiModelProperty(value = "角色标识")
    private String code;

    /** 角色描述 */
    @ApiModelProperty(value = "角色描述")
    private String description;

    /** 数据权限:1-全部,2-自定义,3-本级及子级,4-本级 */
    @ApiModelProperty(value = "数据权限:1-全部,2-自定义,3-本级及子级,4-本级")
    @TableField("data_type")
    private Integer dataType;

    /** 是否删除:0-正常,1-已删除 */
    @ApiModelProperty(value = "是否删除:0-正常,1-已删除")
    @TableField("is_del")
    private Boolean del;

}
