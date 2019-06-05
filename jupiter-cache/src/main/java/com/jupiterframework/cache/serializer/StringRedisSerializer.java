package com.jupiterframework.cache.serializer;

import java.nio.charset.Charset;

import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.util.Assert;


@SuppressWarnings("rawtypes")
public class StringRedisSerializer implements RedisSerializer {
    private static final byte[] EMPTY_ARRAY = new byte[0];

    private final Charset charset;


    public StringRedisSerializer() {
        this(Charset.forName("UTF8"));
    }


    public StringRedisSerializer(Charset charset) {
        Assert.notNull(charset, "Charset must not be null!");
        this.charset = charset;
    }


    @Override
    public String deserialize(byte[] bytes) {
        return (bytes == null ? null : new String(bytes, charset));
    }


    @Override
    public byte[] serialize(Object string) {
        if (string == null)
            return EMPTY_ARRAY;

        if (string instanceof String)
            return ((String) string).getBytes(charset);

        return (string.toString()).getBytes(charset);
    }
}
