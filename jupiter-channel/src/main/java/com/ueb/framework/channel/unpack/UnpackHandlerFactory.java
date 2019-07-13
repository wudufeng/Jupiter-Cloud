package com.ueb.framework.channel.unpack;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;


@Component
public class UnpackHandlerFactory implements ApplicationContextAware {

    private Map<String, UnpackHandler> resolvers = new HashMap<>(2);


    public UnpackHandler create(String key) {
        return resolvers.get(key);
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {

        applicationContext.getBeansOfType(UnpackHandler.class).entrySet().forEach(v -> resolvers.put(v.getKey(), v.getValue()));

    }
}
