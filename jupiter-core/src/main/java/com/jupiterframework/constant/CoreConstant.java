package com.jupiterframework.constant;

public class CoreConstant {
    private CoreConstant() {
    }

    /** session header key */
    public static final String SESSION_KEY = "x-auth-token";

    /** 签名 key */
    public static final String SIGN_KEY = "sign-token";

    /** 使用feignclient时设置到header */
    public static final String CONSUMER_SIDE = "CONSUMER-SIDE";
}
