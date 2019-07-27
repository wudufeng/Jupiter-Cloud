package com.jupiterframework.web.converter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;


@Configuration
public class StringEmptyAsNullConverter implements Converter<String, String> {

    @Value("${spring.mvc.emptyAsNull:true}")
    private boolean emptyAsNull = true;


    @Override
    public String convert(String source) {
        if (this.emptyAsNull && "".equals(source)) {
            return null;
        }
        return source;
    }

}
