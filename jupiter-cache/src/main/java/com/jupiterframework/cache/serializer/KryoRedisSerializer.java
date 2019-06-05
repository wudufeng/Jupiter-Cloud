package com.jupiterframework.cache.serializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.TreeSet;
import java.util.UUID;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.DefaultSerializers;
import com.esotericsoftware.kryo.serializers.JavaSerializer;

import de.javakaffee.kryoserializers.ArraysAsListSerializer;
import de.javakaffee.kryoserializers.BitSetSerializer;
import de.javakaffee.kryoserializers.GregorianCalendarSerializer;
import de.javakaffee.kryoserializers.JdkProxySerializer;
import de.javakaffee.kryoserializers.RegexSerializer;
import de.javakaffee.kryoserializers.SynchronizedCollectionsSerializer;
import de.javakaffee.kryoserializers.URISerializer;
import de.javakaffee.kryoserializers.UUIDSerializer;
import de.javakaffee.kryoserializers.UnmodifiableCollectionsSerializer;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class KryoRedisSerializer implements RedisSerializer<Object> {

    private static final boolean REGISTRATION_REQUIRED = false;
    private static final byte[] EMPTY_ARRAY = new byte[0];

    private static final ThreadLocal<Kryo> KRYO = new ThreadLocal<Kryo>() {
        @Override
        protected Kryo initialValue() {
            Kryo kryo = new Kryo() {
                @Override
                @SuppressWarnings("unchecked")
                public com.esotericsoftware.kryo.Serializer<?> getDefaultSerializer(@SuppressWarnings("rawtypes") Class type) {
                    if (type == null) {
                        throw new IllegalArgumentException("type cannot be null.");
                    }

                    if (!type.isArray()) {
                        boolean checkZeroArgConstructor = false;
                        try {
                            type.getDeclaredConstructor();
                            checkZeroArgConstructor = true;
                        } catch (NoSuchMethodException e) {
                        }
                        if (!checkZeroArgConstructor) {
                            log.warn("{} has no zero-arg constructor and this will affect the serialization performance", type.getName());
                            return new JavaSerializer();
                        }
                    }
                    return super.getDefaultSerializer(type);
                };
            };

            kryo.setRegistrationRequired(REGISTRATION_REQUIRED);

            kryo.register(Arrays.asList("").getClass(), new ArraysAsListSerializer());
            kryo.register(GregorianCalendar.class, new GregorianCalendarSerializer());
            kryo.register(InvocationHandler.class, new JdkProxySerializer());
            kryo.register(BigDecimal.class, new DefaultSerializers.BigDecimalSerializer());
            kryo.register(BigInteger.class, new DefaultSerializers.BigIntegerSerializer());
            kryo.register(Pattern.class, new RegexSerializer());
            kryo.register(BitSet.class, new BitSetSerializer());
            kryo.register(URI.class, new URISerializer());
            kryo.register(UUID.class, new UUIDSerializer());
            UnmodifiableCollectionsSerializer.registerSerializers(kryo);
            SynchronizedCollectionsSerializer.registerSerializers(kryo);

            // now just added some very common classes
            // optimization
            kryo.register(HashMap.class);
            kryo.register(ArrayList.class);
            kryo.register(LinkedList.class);
            kryo.register(HashSet.class);
            kryo.register(TreeSet.class);
            kryo.register(Hashtable.class);
            kryo.register(Date.class);
            kryo.register(Calendar.class);
            kryo.register(ConcurrentHashMap.class);
            kryo.register(SimpleDateFormat.class);
            kryo.register(GregorianCalendar.class);
            kryo.register(Vector.class);
            kryo.register(BitSet.class);
            kryo.register(StringBuffer.class);
            kryo.register(StringBuilder.class);
            kryo.register(Object.class);
            kryo.register(Object[].class);
            kryo.register(String[].class);
            kryo.register(byte[].class);
            kryo.register(char[].class);
            kryo.register(int[].class);
            kryo.register(float[].class);
            kryo.register(double[].class);

            return kryo;
        }
    };


    @Override
    public byte[] serialize(Object t) throws SerializationException {
        if (t == null) {
            return EMPTY_ARRAY;
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream(128);
        Output out = new Output(baos);
        try {
            try {
                KRYO.get().writeClassAndObject(out, t);
            } finally {
                out.close();
            }
            return baos.toByteArray();
        } finally {
            try {
                baos.close();
            } catch (IOException e) {
            }
        }
    }


    @Override
    public Object deserialize(byte[] bytes) throws SerializationException {
        if (bytes == null) {
            return null;
        }

        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        try {
            Input in = new Input(bis);
            try {
                return KRYO.get().readClassAndObject(in);
            } catch (IndexOutOfBoundsException e) {
                return new String(bytes, StandardCharsets.UTF_8);
            } finally {
                in.close();
            }
        } finally {
            try {
                bis.close();
            } catch (IOException e) {
            }
        }
    }

}
