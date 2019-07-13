package com.ueb.framework.channel.resolver.support;

import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Component;

import com.ueb.framework.channel.resolver.ValueResolver;


@Component
public class Base64Md5Resolver implements ValueResolver {

    @Override
    public String resolve(Map<String, String> queryParams, String value) {
        if (value.startsWith("@Base64Md5"))
            return Base64.encodeBase64String(DigestUtils.md5(value.substring(10).trim()));

        return value;
    }

}
