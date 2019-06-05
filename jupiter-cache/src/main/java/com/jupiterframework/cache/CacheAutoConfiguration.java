package com.jupiterframework.cache;

import java.net.UnknownHostException;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import com.jupiterframework.cache.serializer.KryoRedisSerializer;
import com.jupiterframework.cache.serializer.StringRedisSerializer;


@Configuration
@ComponentScan
public class CacheAutoConfiguration {

    @SuppressWarnings("unchecked")
    @Bean
    @ConditionalOnMissingBean(name = "redisTemplate")
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) throws UnknownHostException {
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        KryoRedisSerializer kryoRedisSerializer = new KryoRedisSerializer();

        RedisTemplate<Object, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        template.setDefaultSerializer(kryoRedisSerializer);
        template.setHashKeySerializer(stringRedisSerializer);
        template.setHashValueSerializer(kryoRedisSerializer);
        template.setKeySerializer(stringRedisSerializer);
        template.setValueSerializer(kryoRedisSerializer);
        template.setStringSerializer(stringRedisSerializer);

        return template;
    }

}
