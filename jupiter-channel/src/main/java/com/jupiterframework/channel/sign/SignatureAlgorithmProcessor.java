package com.jupiterframework.channel.sign;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.commons.codec.binary.Hex;
import org.springframework.util.Base64Utils;
import org.springframework.util.StringUtils;

import com.jupiterframework.channel.config.Authorized;
import com.jupiterframework.channel.config.Service;
import com.jupiterframework.channel.pojo.Authorization;

import lombok.extern.slf4j.Slf4j;


/**
 * 签名
 * 
 * @author wudf
 *
 */
@Slf4j
public abstract class SignatureAlgorithmProcessor {

    public String process(Service svccfg, Authorization auth, Map<String, String> params) {

        byte[] src = this.digest(svccfg, auth, params);

        String encode = null;

        Authorized authCfg = svccfg.getChannel().getAuthorized();
        switch (authCfg.getByteConvert()) {
        case HEX:
            encode = Hex.encodeHexString(src);
            break;
        case BASE64:
            encode = Base64Utils.encodeToString(src);
            break;
        default:
            throw new IllegalArgumentException("不支持的字节转换方式" + authCfg.getByteConvert());
        }

        String signStr = authCfg.isUpperCase() ? encode.toUpperCase() : encode;
        log.debug("生成的签名串 {}", signStr);
        return signStr;
    }


    protected abstract byte[] digest(Service svccfg, Authorization auth, Map<String, String> params);


    protected String generateSortedParamString(Service svccfg, Authorization auth, Map<String, String> params) {
        Map<String, String> sorted = new TreeMap<>();
        sorted.putAll(params);

        // 忽略不需要参与签名的参数
        for (String ignore : svccfg.getChannel().getAuthorized().getIgnoreProperties()) {
            sorted.remove(ignore);
        }

        Authorized authCfg = svccfg.getChannel().getAuthorized();

        StringBuilder query = new StringBuilder();
        if (StringUtils.hasText(authCfg.getPrefix())) {
            query.append(authCfg.getPrefix().replace("@URL", auth.getUrl() + svccfg.getPath()).replace("@PATH", svccfg.getPath())
                .replace("@HOST", URI.create(auth.getUrl()).getHost()).replace("@SECURET", auth.getSecuretKey()));
        }

        Iterator<Entry<String, String>> pairs = sorted.entrySet().iterator();
        while (pairs.hasNext()) {
            Map.Entry<String, String> pair = pairs.next();

            query.append(pair.getKey()).append(authCfg.getPairs()).append(authCfg.isEncodeValue() ? urlEncode(pair.getValue()) : pair.getValue());
            if (pairs.hasNext())
                query.append(authCfg.getSplit());
        }

        if (StringUtils.hasText(authCfg.getSuffix())) {
            query.append(authCfg.getSuffix().replace("@URL", auth.getUrl() + svccfg.getPath()).replace("@PATH", svccfg.getPath())
                .replace("@HOST", URI.create(auth.getUrl()).getHost()).replace("@SECURET", auth.getSecuretKey()));
        }

        log.debug("sign query {}", query);

        return query.toString();

    }


    private String urlEncode(String rawValue) {
        String value = (rawValue == null) ? "" : rawValue;
        try {
            return URLEncoder.encode(value, StandardCharsets.UTF_8.displayName()).replace("+", "%20").replace("*", "%2A").replace("%7E", "~");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException("", e);
        }

    }
}
