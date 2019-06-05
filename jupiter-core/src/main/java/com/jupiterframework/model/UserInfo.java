package com.jupiterframework.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 用户登录的基本信息，该对象会在整个链路里传递
 * 
 * @author wudf
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo implements Serializable {
    private static final long serialVersionUID = -7073358782522431352L;

    public static final String HEADER_KEY = "LOGIN_USER";

    private String sessionId;

    /** 当前登录用户ID */
    private Long userId;

    /** 当前用户登录系统ID */
    private String systemId;

    /** 当前用户登录的公司ID */
    private String companyId;
}
