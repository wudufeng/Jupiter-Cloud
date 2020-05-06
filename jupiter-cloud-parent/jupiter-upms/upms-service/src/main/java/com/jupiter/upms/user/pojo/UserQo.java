package com.jupiter.upms.user.pojo;

import java.util.Date;

import javax.validation.constraints.NotBlank;

import lombok.Data;


/** 用户参数 */
@Data
public class UserQo {

    @NotBlank
    private String loginName;
    @NotBlank
    private String password;

    private String realname;

    private String sex;

    private String phone;
    private String email;

    private Date birthday;
}
