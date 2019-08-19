package com.jupiter.upms.exception;

import com.jupiterframework.constant.ExceptionDefinition;


public enum UpmsExceptionCodeEnum implements ExceptionDefinition {
    ORGANIZATION_NOT_EXISTS(4001, "组织机构编码[{}]不存在");

    private int code;
    private String message;


    private UpmsExceptionCodeEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }


    @Override
    public int getCode() {
        return code;
    }


    @Override
    public String getMessage() {
        return message;
    }

}
