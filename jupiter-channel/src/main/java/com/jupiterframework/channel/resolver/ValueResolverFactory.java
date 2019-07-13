package com.jupiterframework.channel.resolver;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;


@Component
public class ValueResolverFactory implements ApplicationContextAware {

    private Map<String, ValueResolver> resolvers = new HashMap<>(1);


    public ValueResolver create(String key) {
        return resolvers.get(key);
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {

        applicationContext.getBeansOfType(ValueResolver.class).entrySet().forEach(v -> resolvers.put(v.getKey(), v.getValue()));

    }
}
