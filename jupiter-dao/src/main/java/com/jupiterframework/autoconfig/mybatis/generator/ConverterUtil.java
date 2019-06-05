package com.jupiterframework.autoconfig.mybatis.generator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 下划线转驼峰命名
 *
 */
public final class ConverterUtil {
    private static final Pattern pattern = Pattern.compile("([A-Za-z\\d]+)(_)?");


    private ConverterUtil() {
    }


    public static String translateClassName(String text) {
        return translate(text, false);
    }


    public static String translateFieldName(String text) {
        return translate(text, true);
    }


    /**
     * 
     * @param text
     *            需要转换的带下划线的文本
     * @param smallCamel
     *            首字母是否小写
     * @return
     */
    public static String translate(String text, boolean smallCamel) {
        if (text == null || "".equals(text)) {
            return "";
        }
        if (text.startsWith("is_"))
            text = text.substring(3);
        StringBuilder sb = new StringBuilder();
        Matcher matcher = pattern.matcher(text.toLowerCase());
        while (matcher.find()) {
            String word = matcher.group();
            sb.append(smallCamel && matcher.start() == 0 ? Character.toLowerCase(word.charAt(0)) : Character.toUpperCase(word.charAt(0)));
            int index = word.lastIndexOf('_');
            if (index > 0) {
                sb.append(word.substring(1, index));
            } else {
                sb.append(word.substring(1));
            }
        }
        return sb.toString();
    }

}
