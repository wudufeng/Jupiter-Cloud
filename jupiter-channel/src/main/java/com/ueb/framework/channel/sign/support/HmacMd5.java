package com.ueb.framework.channel.sign.support;

import java.util.Map;

import org.apache.commons.codec.digest.HmacUtils;
import org.springframework.stereotype.Component;

import com.ueb.framework.channel.config.Service;
import com.ueb.framework.channel.pojo.Authorization;
import com.ueb.framework.channel.sign.SignatureAlgorithmProcessor;


@Component
public class HmacMd5 extends SignatureAlgorithmProcessor {

    @Override
    public byte[] digest(Service svccfg, Authorization auth, Map<String, String> params) {
        return HmacUtils.hmacMd5(auth.getSecuretKey(), generateSortedParamString(svccfg, auth, params));
    }

}
