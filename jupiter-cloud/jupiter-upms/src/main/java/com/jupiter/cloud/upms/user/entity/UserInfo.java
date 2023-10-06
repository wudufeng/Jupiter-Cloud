package com.jupiter.cloud.upms.user.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.jupiterframework.model.GenericPo;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;


/**
 * 用户信息
 *
 * @author jupiter
 * @since 2020-04-25
 */
@lombok.Data
@lombok.NoArgsConstructor
@lombok.EqualsAndHashCode(callSuper = false)
@Schema
@TableName("user_info")
public class UserInfo extends GenericPo {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    @TableId("user_id")
    private Long userId;

    /** 昵称 */
    private String nickname;

    /** 姓名 */
    private String realname;

    /** 性别:0-未知,1-男,2-女 */
    private String sex;

    /** 头像url */
    @TableField("head_img_url")
    private String headImgUrl;

    /** 手机号码 */
    private String phone;

    /** 邮箱 */
    private String email;

    /** 出生日期 */
    private Date birthday;

    /** 证件类型:1-身份证 */
    @TableField("id_type")
    private Integer idType;

    /** 证件号码 */
    @TableField("id_code")
    private String idCode;

    /** 所在国家 */
    private String country;

    /** 所在省份 */
    private String province;

    /** 所在城市 */
    private String city;

    /** 住址 */
    private String address;

    /** 个性签名 */
    private String signature;

    /** 用户来源 */
    @TableField("src_type")
    private Integer srcType;

    /** 状态:1-正常,2-锁定,3-注销 */
    private String status;

}
