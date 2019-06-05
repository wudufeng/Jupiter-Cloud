package com.jupiterframework.util;

import java.util.regex.Pattern;

import com.jupiterframework.constant.PatternEnum;


/**
 * 正则工具类
 * 
 * @author weiem
 *
 */
public class PatternUtils {

    public static boolean matches(PatternEnum patternEnum, String input) {
        return Pattern.matches(patternEnum.getCode(), input);
    }
}
