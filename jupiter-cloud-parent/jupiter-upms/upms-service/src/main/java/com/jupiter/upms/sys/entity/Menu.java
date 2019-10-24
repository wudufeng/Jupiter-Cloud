package com.jupiter.upms.sys.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.jupiterframework.model.GenericPo;

import io.swagger.annotations.ApiModelProperty;


/**
 * 菜单管理
 *
 * @author WUDUFENG
 * @since 2019-08-22
 */
@lombok.Data
@lombok.EqualsAndHashCode(callSuper = true)
@lombok.NoArgsConstructor
@TableName("sys_menu")
public class Menu extends GenericPo {

    private static final long serialVersionUID = 1L;

    /** 菜单ID */
    @ApiModelProperty(value = "菜单ID")
    private Long id;

    /** 菜单编码，从第一层开始拼接，同级使用16进制两位数递增 */
    @ApiModelProperty(value = "菜单编码，从第一层开始拼接，同级使用16进制两位数递增")
    private String code;

    /** 上级菜单编号 */
    @ApiModelProperty(value = "上级菜单编号")
    @TableField("parent_code")
    private String parentCode;

    /** 菜单名称 */
    @ApiModelProperty(value = "菜单名称")
    private String name;

    /** 前端URL */
    @ApiModelProperty(value = "前端URL")
    private String path;

    /** 图标 */
    @ApiModelProperty(value = "图标")
    private String icon;

    /** VUE页面 */
    @ApiModelProperty(value = "VUE页面")
    private String component;

    /** 排序值 */
    @ApiModelProperty(value = "排序值")
    private Integer sort;

    /** 路由缓存:0-开启,1-关闭 */
    @ApiModelProperty(value = "路由缓存:0-开启,1-关闭")
    @TableField("keep_alive")
    private String keepAlive;

    /** 菜单类型:0-菜单,1-按钮） */
    @ApiModelProperty(value = "菜单类型:0-菜单,1-按钮）")
    @TableField("menu_type")
    private String menuType;

    /** 菜单权限标识 */
    @ApiModelProperty(value = "菜单权限标识")
    private String permission;

    /** 是否删除: 0-正常,1-已删除 */
    @ApiModelProperty(value = "是否删除: 0-正常,1-已删除")
    @TableField("is_del")
    private Boolean del;


    public Menu(String parentCode, String code) {
        super();
        this.parentCode = parentCode;
        this.code = code;
    }

}
