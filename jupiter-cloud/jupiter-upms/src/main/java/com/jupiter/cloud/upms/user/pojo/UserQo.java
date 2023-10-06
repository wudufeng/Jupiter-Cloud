package com.jupiter.cloud.upms.user.pojo;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.Date;


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
