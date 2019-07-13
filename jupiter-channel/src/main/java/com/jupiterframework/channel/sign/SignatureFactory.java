package com.jupiterframework.channel.sign;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;


@Component
public class SignatureFactory implements ApplicationContextAware {

    private Map<String, SignatureAlgorithmProcessor> processor = new HashMap<>(6);


    public SignatureAlgorithmProcessor create(String sign) {
        SignatureAlgorithmProcessor p = processor.get(sign);
        if (p == null) {
            throw new IllegalArgumentException("无此签名方式[" + sign + "]");
        }
        return p;
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        applicationContext.getBeansOfType(SignatureAlgorithmProcessor.class).entrySet().forEach(a -> processor.put(a.getKey(), a.getValue()));
    }
}
