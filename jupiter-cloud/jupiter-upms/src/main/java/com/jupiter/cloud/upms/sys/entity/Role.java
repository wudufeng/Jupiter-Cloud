package com.jupiter.cloud.upms.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.jupiterframework.model.GenericPo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.JdbcType;


/**
 * 角色管理
 *
 * @author jupiter
 * @since 2019-10-25
 */
@lombok.Data
@NoArgsConstructor
@lombok.EqualsAndHashCode(callSuper = true)
@TableName("sys_role")
@Schema
public class Role extends GenericPo {

    private static final long serialVersionUID = 1L;

    /** 角色ID */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 租户编码 */
    @TableField(value = "tenant_id", jdbcType = JdbcType.INTEGER)
    private Long tenantId;

    /** 角色名称 */
    private String name;

    /** 角色标识 */
    private String code;

    /** 角色描述 */
    private String description;

    /** 数据权限:1-全部,2-自定义,3-本级及子级,4-本级 */
    @TableField("data_type")
    private Integer dataType;

    /** 是否删除:0-正常,1-已删除 */
    @TableField("is_del")
    private Boolean del;


    public Role(Long tenantId) {
        super();
        this.tenantId = tenantId;
    }

}
