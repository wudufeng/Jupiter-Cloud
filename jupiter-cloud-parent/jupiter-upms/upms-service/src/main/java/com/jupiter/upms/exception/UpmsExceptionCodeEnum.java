package com.jupiter.upms.exception;

import com.jupiterframework.constant.ExceptionDefinition;


public enum UpmsExceptionCodeEnum implements ExceptionDefinition {
    ORGANIZATION_NOT_EXISTS(4001, "组织机构编码[{}]不存在"),
    ORGANIZATION_ILLIGAL_LEVEL(4002, "组织机构不能超过{}层"),
    ORGANIZATION_EXIST_CHILD(4003, "请先删除下属机构"),

    MENU_NOT_EXISTS(4011, "菜单编码[{}]不存在"),
    MENU_EXIST_CHILD(4013, "请先删除子菜单"),

    ROLE_NOT_EXISTS(4011, "角色编码[{}]不存在"),

    USER_EXISTS(4020, "用户名[{}]已存在"),
    USER_NOT_EXISTS(4021, "用户[{}]不存在"),

    ;

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
