package com.jupiter.upms.sys.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.jupiterframework.model.GenericPo;

import io.swagger.annotations.ApiModelProperty;


/**
 * 岗位信息
 *
 * @author WUDUFENG
 * @since 2020-04-24
 */
@lombok.Data
@lombok.NoArgsConstructor
@lombok.EqualsAndHashCode(callSuper = false)
@TableName("sys_post")
public class Post extends GenericPo {

    private static final long serialVersionUID = 1L;

    /** 岗位编码 */
    @ApiModelProperty(value = "岗位编码")
    private Long id;

    /** 租户编码 */
    @ApiModelProperty(value = "租户编码")
    @TableField("tenant_id")
    private Long tenantId;

    /** 岗位名称 */
    @ApiModelProperty(value = "岗位名称")
    private String name;

    /** 岗位描述 */
    @ApiModelProperty(value = "岗位描述")
    @TableField("description")
    private String description;

    /** 排序 */
    @ApiModelProperty(value = "排序")
    private Integer sort;

    /** 岗位类型:1-管理岗,2-技术岗,3-高层,4-基层 */
    @ApiModelProperty(value = "岗位类型:1-管理岗,2-技术岗,3-高层,4-基层")
    private Integer type;

    /** 是否删除: 0-正常,1-已删除 */
    @ApiModelProperty(value = "是否删除: 0-正常,1-已删除")
    @TableField("is_del")
    private Boolean del;

}
