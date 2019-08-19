package com.jupiter.upms.user.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.jupiterframework.model.GenericPo;

import io.swagger.annotations.ApiModelProperty;


/**
 * 用户信息
 *
 * @author WUDUFENG
 * @since 2019-08-12
 */
@lombok.Data
@lombok.EqualsAndHashCode(callSuper = true)
@TableName("user_info")
public class UserInfo extends GenericPo {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    @ApiModelProperty(value = "主键ID")
    @TableId("user_id")
    private Long userId;

    /** 用户名 */
    @ApiModelProperty(value = "用户名")
    private String username;

    /** 登录密码 */
    @ApiModelProperty(value = "登录密码")
    private String password;

    /** 昵称 */
    @ApiModelProperty(value = "昵称")
    private String nickname;

    /** 姓名 */
    @ApiModelProperty(value = "姓名")
    private String realname;

    /** 性别：0-未知 1-男 2-女 */
    @ApiModelProperty(value = "性别：0-未知 1-男 2-女")
    private String sex;

    /** 头像url */
    @ApiModelProperty(value = "头像url")
    @TableField("head_img_url")
    private String headImgUrl;

    /** 手机号码 */
    @ApiModelProperty(value = "手机号码")
    private String phone;

    /** Email */
    @ApiModelProperty(value = "Email")
    private String email;

    /** 所在国家 */
    @ApiModelProperty(value = "所在国家")
    private String country;

    /** 所在省份 */
    @ApiModelProperty(value = "所在省份")
    private String province;

    /** 所在城市 */
    @ApiModelProperty(value = "所在城市")
    private String city;

    /** 住址 */
    @ApiModelProperty(value = "住址")
    private String address;

    /** 个性签名 */
    @ApiModelProperty(value = "个性签名")
    private String signature;

    /** 状态：1-正常，2-锁定,3-注销 */
    @ApiModelProperty(value = "状态：1-正常，2-锁定,3-注销")
    private String status;


}
