package com.jupiterframework.web.converter;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;

import com.jupiterframework.util.StringUtils;


@Configuration
public class StringDateConverter implements Converter<String, Date> {
    private static final String[] FORMAT =
            new String[] { "yyyyMMddHHmmss", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd'T'HH:mm:ss+0000", "EEE MMM dd HH:mm:ss zzz yyyy", "yyyyMMdd", "yyyy-MM-dd" };


    @Override
    public Date convert(String source) {
        if (StringUtils.isBlank(source))
            return null;
        try {
            // CST时区格式 EEE MMM dd HH:mm:ss zzz yyyy 必须是Locale.US
            return DateUtils.parseDate(source, Locale.US, FORMAT);
        } catch (ParseException e) {
            throw new IllegalArgumentException(source + " parse Date error ,format " + Arrays.toString(FORMAT));
        }
    }

}
