package com.jupiter.cloud.upms.user.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.jupiterframework.model.GenericPo;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;


/**
 * 用户登录信息
 *
 * @author jupiter
 * @since 2020-04-25
 */
@lombok.Data
@lombok.NoArgsConstructor
@lombok.EqualsAndHashCode(callSuper = false)
@TableName("user_login")
@Schema
public class UserLogin extends GenericPo {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    @TableId("user_id")
    private Long userId;

    /** 用户名 */
    @TableField("login_name")
    private String loginName;

    /** 登录密码 */
    private String password;

    /** 是否允许手机号码登录:0-否,1-是 */
    @TableField("phone_login_flag")
    private Boolean phoneLoginFlag;

    /** 是否允许EMAIL登录:0-否,1-是 */
    @TableField("email_login_flag")
    private Boolean emailLoginFlag;

    /** 是否需要重设密码:0-否,1-是 */
    @TableField("pwd_reset")
    private Boolean pwdReset;

    /** 连续错误次数 */
    @TableField("error_count")
    private Integer errorCount;

    /** 首次登录时间 */
    @TableField("first_login_time")
    private Date firstLoginTime;

    /** 最近登录时间 */
    @TableField("latest_login_time")
    private Date latestLoginTime;


    public UserLogin(String loginName) {
        super();
        this.loginName = loginName;
    }

}
