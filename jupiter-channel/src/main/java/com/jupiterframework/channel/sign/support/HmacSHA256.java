package com.jupiterframework.channel.sign.support;

import java.util.Map;

import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.jupiterframework.channel.config.Service;
import com.jupiterframework.channel.pojo.Authorization;
import com.jupiterframework.channel.sign.SignatureAlgorithmProcessor;


@Component
public class HmacSHA256 extends SignatureAlgorithmProcessor {

    @Override
    public byte[] digest(Service svccfg, Authorization auth, Map<String, String> params) {
        if (StringUtils.isBlank(auth.getSecuretKey()))
            throw new IllegalArgumentException("授权信息securetKey不能为空!");
        return new HmacUtils(HmacAlgorithms.HMAC_SHA_256, auth.getSecuretKey())
            .hmac(this.generateSortedParamString(svccfg, auth, params));
    }

}
