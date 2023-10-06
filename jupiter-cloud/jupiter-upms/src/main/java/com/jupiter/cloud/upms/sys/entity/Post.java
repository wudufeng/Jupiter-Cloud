package com.jupiter.cloud.upms.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.jupiterframework.model.GenericPo;
import io.swagger.v3.oas.annotations.media.Schema;
import org.apache.ibatis.type.JdbcType;


/**
 * 岗位信息
 *
 * @author jupiter
 * @since 2020-04-24
 */
@lombok.Data
@lombok.NoArgsConstructor
@lombok.EqualsAndHashCode(callSuper = false)
@TableName("sys_post")
@Schema
public class Post extends GenericPo {

    private static final long serialVersionUID = 1L;

    /** 岗位编码 */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 租户编码 */
    @TableField(value = "tenant_id", jdbcType = JdbcType.INTEGER)
    private Long tenantId;

    /** 岗位名称 */
    private String name;

    /** 岗位描述 */
    @TableField("description")
    private String description;

    /** 排序 */
    private Integer sort;

    /** 岗位类型:1-管理岗,2-技术岗,3-高层,4-基层 */
    private Integer type;

    /** 是否删除: 0-正常,1-已删除 */
    @TableField("is_del")
    private Boolean del;

}
