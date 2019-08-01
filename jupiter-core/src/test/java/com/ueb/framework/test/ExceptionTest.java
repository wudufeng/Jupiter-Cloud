package com.ueb.framework.test;

import org.junit.Test;

import com.jupiterframework.constant.ExceptionDefinition;
import com.jupiterframework.exception.BusinessException;

import lombok.extern.slf4j.Slf4j;


@Slf4j
public class ExceptionTest {

    @Test
    public void testException() {
        try {
            throw new BusinessException(SimpleExceptionTestEnum.METHOD_INVALID, "get");
        } catch (Exception e) {
            log.debug("", e);
        }

    }

}


enum SimpleExceptionTestEnum implements ExceptionDefinition {

    METHOD_INVALID(201, "不支持的请求方式{}");

    private final int code;
    private final String message;


    SimpleExceptionTestEnum(int code, String message) {
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
