package com.jupiter.upms.sys.entity;

import org.apache.ibatis.type.JdbcType;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.jupiterframework.model.GenericPo;

import io.swagger.annotations.ApiModelProperty;


/**
 * 一对多关系映射表
 *
 * @author WUDUFENG
 * @since 2020-04-27
 */
@lombok.Data
@lombok.NoArgsConstructor
@lombok.EqualsAndHashCode(callSuper = false)
@TableName("sys_data_relative")
public class DataRelative extends GenericPo {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    @TableField(jdbcType = JdbcType.INTEGER)
    private Integer id;

    /** 范围ID */
    @ApiModelProperty(value = "范围ID")
    @TableField("scope_id")
    private Long scopeId;

    /** 实例ID */
    @ApiModelProperty(value = "实例ID")
    @TableField("instance_id")
    private Long instanceId;

    /** 关联类型:1-角色菜单,2-角色自定义数据权限,3-机构角色 */
    @ApiModelProperty(value = "关联类型:1-角色菜单,2-角色自定义数据权限,3-机构角色,4-员工/用户角色")
    @TableField("ref_type")
    private Integer refType;

    /** 是否启用:1-启用,0-禁用 */
    @ApiModelProperty(value = "是否启用:1-启用,0-禁用")
    @TableField("is_enable")
    private Boolean enable;


    public DataRelative(Long scopeId) {
        super();
        this.scopeId = scopeId;
    }


    public DataRelative(Long scopeId, Long instanceId) {
        super();
        this.scopeId = scopeId;
        this.instanceId = instanceId;
    }

}
