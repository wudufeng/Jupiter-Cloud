package com.jupiter.upms.sys.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.jupiterframework.model.GenericPo;

import io.swagger.annotations.ApiModelProperty;


/**
 * 组织架构
 *
 * @author WUDUFENG
 * @since 2019-08-09
 */
@lombok.Data
@lombok.EqualsAndHashCode(callSuper = true)
@lombok.NoArgsConstructor
@TableName("sys_organization")
public class Organization extends GenericPo {

    private static final long serialVersionUID = 1L;

    /** 机构ID */
    @ApiModelProperty(value = "机构ID")
    @TableId
    @TableField(el = "jdbcType=Long")
    private Long id;

    /** 租户编码 */
    @ApiModelProperty(value = "租户编码")
    @TableField(value = "tenant_id")
    private Long tenantId;

    /** 机构编码，从第一层开始拼接，同级机构使用16进制两位数递增 */
    @ApiModelProperty(value = "机构编码，从第一层开始拼接，同级机构使用16进制两位数递增")
    private String code;

    /** 机构层级 */
    @ApiModelProperty(value = "机构层级")
    private Integer level;

    /** 机构名称 */
    @ApiModelProperty(value = "机构名称")
    private String name;

    /** 上级机构编号 */
    @ApiModelProperty(value = "上级机构编号")
    @TableField("parent_code")
    private String parentCode;

    /** 排序 */
    @ApiModelProperty(value = "排序")
    private Integer sort;

    /** 是否删除 0：正常, 1：已删除 */
    @ApiModelProperty(value = "是否删除 0：正常, 1：已删除")
    @TableField("is_del")
    private Boolean del;


    public Organization(Long tenantId) {
        super();
        this.tenantId = tenantId;
        this.del = Boolean.FALSE;
    }


    public Organization(String code, Long tenantId) {
        this(tenantId);
        this.code = code;
    }


    public Organization(Long tenantId, String parentCode) {
        this(tenantId);
        this.parentCode = parentCode;
    }

}
