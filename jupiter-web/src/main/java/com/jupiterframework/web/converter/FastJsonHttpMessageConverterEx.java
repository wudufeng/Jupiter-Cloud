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

import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
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
    public void write(Object o, Type type, MediaType contentType, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {

        for (ServiceResponseFilter filter : serviceFilterContext.getSvcRespFilters()) {
            filter.doFilter(o);
        }
        if (!(outputMessage instanceof ServletServerHttpResponse)) {
            log.debug(outputMessage.getClass().getName());
        }
        super.write(o, type, contentType, outputMessage);
    }

}
