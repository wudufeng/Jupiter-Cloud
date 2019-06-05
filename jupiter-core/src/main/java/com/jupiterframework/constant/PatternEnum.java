package com.jupiterframework.constant;

import lombok.Getter;


/**
 * 系统enum
 * 
 * @author hesp
 *
 */
@Getter
public enum PatternEnum {

    // 密码
    PASSWORD("^[0-9A-Za-z`~!@#$^&*()=|{}':;'-_,\\[\\].<>/?~]{6,14}$"),

    // 手机
    MOBILE_PHONE("^[1][3,4,5,7,8,9][0-9]{9}$"),

    // 邮箱
    EMAIL("^[1][3,4,5,7,8,9][0-9]{9}$"),

    // 身份证
    IDCARD("^[1][3,4,5,7,8,9][0-9]{9}$"),

    // 整数
    INTEGER("^[-\\+]?[\\d]*$"),

    // 小数
    FLOAT("^\\d+(\\.\\d+)?$"),;

    private String code;


    PatternEnum(String code) {
        this.code = code;
    }
}
