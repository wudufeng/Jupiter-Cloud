package com.jupiter.cloud.upms.sys.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.jupiterframework.model.GenericPo;
import io.swagger.v3.oas.annotations.media.Schema;


/**
 * 菜单管理
 *
 * @author jupiter
 * @since 2019-08-22
 */
@lombok.Data
@lombok.EqualsAndHashCode(callSuper = true)
@lombok.NoArgsConstructor
@TableName("sys_menu")
@Schema
public class Menu extends GenericPo {

    private static final long serialVersionUID = 1L;

    /** 最顶层菜单的parentCode字段值 */
    public static final String TOP_LEVEL_PARENT_CODE = "";

    /** 菜单ID */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 菜单编码，从第一层开始拼接，同级使用16进制两位数递增 */
    private String code;

    /** 上级菜单编号 */
    @TableField(value = "parent_code", whereStrategy = FieldStrategy.NOT_NULL)
    private String parentCode;

    /** 菜单名称 */
    private String name;

    /** 前端URL */
    private String path;

    /** 图标 */
    private String icon;

    /** VUE页面 */
    private String component;

    /** 排序值 */
    private Integer sort;

    /** 路由缓存:0-开启,1-关闭 */
    @TableField("keep_alive")
    private String keepAlive;

    /** 菜单类型:0-菜单,1-按钮） */
    @TableField("menu_type")
    private String menuType;

    /** 菜单权限标识 */
    private String permission;

    /** 是否删除: 0-正常,1-已删除 */
    @TableField("is_del")
    private Boolean del;


    public Menu(String parentCode, String code) {
        super();
        this.parentCode = parentCode;
        this.code = code;
    }


    public String getLabel() {
        return this.name;
    }
}
