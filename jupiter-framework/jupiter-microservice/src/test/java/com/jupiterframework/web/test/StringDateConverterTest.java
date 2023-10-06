package com.jupiterframework.web.test;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.text.ParseException;
import java.util.Date;

public class StringDateConverterTest {
    public static void main(String[] args) throws ParseException {
        String formatDate = DateFormatUtils.format(new Date(), "yyyy-MM-dd'T'HH:mm:ssZZ");
        System.out.println(formatDate);
        Date date = DateUtils.parseDate( formatDate, DateFormatUtils.ISO_8601_EXTENDED_DATETIME_TIME_ZONE_FORMAT.getPattern());
        System.out.println(date);
    }
}
