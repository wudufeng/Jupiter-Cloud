package com.jupiterframework.web.converter;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.server.ServletServerHttpResponse;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.parser.deserializer.ArrayListTypeFieldDeserializer;
import com.alibaba.fastjson.parser.deserializer.DefaultFieldDeserializer;
import com.alibaba.fastjson.parser.deserializer.FieldDeserializer;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.alibaba.fastjson.serializer.StringCodec;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.alibaba.fastjson.util.FieldInfo;
import com.alibaba.fastjson.util.JavaBeanInfo;
import com.jupiterframework.filter.ServiceResponseFilter;
import com.jupiterframework.filter.support.ServiceFilterContext;
import com.jupiterframework.util.BeanUtils;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Configuration
public class FastJsonHttpMessageConverterEx extends FastJsonHttpMessageConverter {

    @Resource
    private ServiceFilterContext serviceFilterContext;


    public FastJsonHttpMessageConverterEx() {

        FastJsonParseConfig pConfig = new FastJsonParseConfig();
        pConfig.putDeserializer(String.class, new StringEmptyAsNullDeseializer());
        BeanUtils.FASTJSON_CONFIG.setParserConfig(pConfig);

        setFastJsonConfig(BeanUtils.FASTJSON_CONFIG);

        List<MediaType> supportedMediaTypes = new ArrayList<>();
        supportedMediaTypes.add(MediaType.APPLICATION_JSON);
        supportedMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
        supportedMediaTypes.add(MediaType.TEXT_PLAIN);
        setSupportedMediaTypes(supportedMediaTypes);
    }


    @Override
    public Object read(Type type, Class<?> contextClass, HttpInputMessage inputMessage) throws IOException {

        return super.read(type, contextClass, inputMessage);
    }


    @Override
    public void write(Object o, Type type, MediaType contentType, HttpOutputMessage outputMessage)
            throws IOException, HttpMessageNotWritableException {

        for (ServiceResponseFilter filter : serviceFilterContext.getSvcRespFilters()) {
            filter.doFilter(o);
        }
        if (!(outputMessage instanceof ServletServerHttpResponse)) {
            log.debug(outputMessage.getClass().getName());
        }
        super.write(o, type, contentType, outputMessage);
    }

    public static class FastJsonParseConfig extends ParserConfig {
        @Override
        public FieldDeserializer createFieldDeserializer(ParserConfig mapping, JavaBeanInfo beanInfo,
                FieldInfo fieldInfo) {

            Class<?> clazz = beanInfo.clazz;
            Class<?> fieldClass = fieldInfo.fieldClass;

            Class<?> deserializeUsing = null;
            JSONField annotation = fieldInfo.getAnnotation();
            if (annotation != null) {
                deserializeUsing = annotation.deserializeUsing();
                if (deserializeUsing == Void.class) {
                    deserializeUsing = null;
                }
            }

            if (deserializeUsing == null && (fieldClass == List.class || fieldClass == ArrayList.class)) {
                return new ArrayListTypeFieldDeserializer(mapping, clazz, fieldInfo);
            }

            if (fieldInfo.fieldType == String.class) {
                return new StringFieldEmptyAsNullDeserializer(mapping, clazz, fieldInfo);
            }
            return new DefaultFieldDeserializer(mapping, clazz, fieldInfo);
        }
    }

    public static class StringEmptyAsNullDeseializer implements ObjectDeserializer {

        @SuppressWarnings("unchecked")
        @Override
        public String deserialze(DefaultJSONParser parser, Type clazz, Object fieldName) {
            String val = StringCodec.instance.deserialze(parser, clazz, fieldName);
            if (val instanceof String && "".equals(val)) {
                return null;
            }
            return val;
        }


        @Override
        public int getFastMatchToken() {
            return StringCodec.instance.getFastMatchToken();
        }

    }

    public static class StringFieldEmptyAsNullDeserializer extends DefaultFieldDeserializer {

        public StringFieldEmptyAsNullDeserializer(ParserConfig config, Class<?> clazz, FieldInfo fieldInfo) {
            super(config, clazz, fieldInfo);

            if (fieldValueDeserilizer == null && fieldInfo.fieldType == String.class) {
                fieldValueDeserilizer = config.getDeserializer(String.class);
                customDeserilizer = true;
            }
        }


        @Override
        public void setValue(Object object, String value) {
            if ("".equals(value)) {
                super.setValue(object, null);
            } else {
                super.setValue(object, value);
            }
        }


        @Override
        public void setValue(Object object, Object value) {
            if ("".equals(value)) {
                super.setValue(object, null);
            } else {
                super.setValue(object, value);
            }
        }

    }
}
