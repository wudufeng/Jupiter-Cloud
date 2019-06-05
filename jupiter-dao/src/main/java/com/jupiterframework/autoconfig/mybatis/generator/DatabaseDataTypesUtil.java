package com.jupiterframework.autoconfig.mybatis.generator;

import java.sql.Types;
import java.util.HashMap;


/**
 * 
 * 数据库类型、java类型转换
 */
public class DatabaseDataTypesUtil {

    private final static IntStringMap PREFERRED_JAVA_SQL_TYPE = new IntStringMap();


    public static boolean isFloatNumber(String javaType) {
        if (javaType.endsWith("Float") || javaType.endsWith("Double") || javaType.endsWith("BigDecimal") || javaType.endsWith("BigInteger")) {
            return true;
        }
        if (javaType.endsWith("float") || javaType.endsWith("double") || javaType.endsWith("BigDecimal") || javaType.endsWith("BigInteger")) {
            return true;
        }
        return false;
    }


    public static boolean isIntegerNumber(String javaType) {
        if (javaType.endsWith("Long") || javaType.endsWith("Integer") || javaType.endsWith("Short") || javaType.endsWith("Byte")) {
            return true;
        }
        if (javaType.endsWith("long") || javaType.endsWith("int") || javaType.endsWith("short") || javaType.endsWith("byte")) {
            return true;
        }
        return false;
    }


    public static boolean isDate(String javaType) {
        if (javaType.endsWith("Date") || javaType.endsWith("Timestamp") || javaType.endsWith("Time")) {
            return true;
        }
        return false;
    }


    public static boolean isString(String javaType) {
        if (javaType.endsWith("String")) {
            return true;
        }
        return false;
    }


    public static String getPreferredJavaType(int sqlType, int size, String decimalDigits) {
        if ((sqlType == Types.DECIMAL || sqlType == Types.NUMERIC) && "0".equals(decimalDigits)) {
            if (size == 1) {
                return "Boolean";
            } else if (size < 3) {
                return "Byte";
            } else if (size < 5) {
                return "Short";
            } else if (size < 10) {
                return "Integer";
            } else if (size < 19) {
                return "Long";
            } else {
                return "java.math.BigDecimal";
            }
        }

        String result = PREFERRED_JAVA_SQL_TYPE.getString(sqlType);
        if (result == null) {
            throw new RuntimeException("unsuport sql type " + sqlType);
        }
        return result;
    }

    static {
        PREFERRED_JAVA_SQL_TYPE.put(Types.TINYINT, "Boolean");
        PREFERRED_JAVA_SQL_TYPE.put(Types.SMALLINT, "Integer");
        PREFERRED_JAVA_SQL_TYPE.put(Types.INTEGER, "Integer");
        PREFERRED_JAVA_SQL_TYPE.put(Types.BIGINT, "Long");
        PREFERRED_JAVA_SQL_TYPE.put(Types.REAL, "Float");
        PREFERRED_JAVA_SQL_TYPE.put(Types.FLOAT, "Double");
        PREFERRED_JAVA_SQL_TYPE.put(Types.DOUBLE, "Double");
        PREFERRED_JAVA_SQL_TYPE.put(Types.DECIMAL, "java.math.BigDecimal");
        PREFERRED_JAVA_SQL_TYPE.put(Types.NUMERIC, "java.math.BigDecimal");
        PREFERRED_JAVA_SQL_TYPE.put(Types.BIT, "Boolean");
        PREFERRED_JAVA_SQL_TYPE.put(Types.BOOLEAN, "Boolean");
        PREFERRED_JAVA_SQL_TYPE.put(Types.CHAR, "String");
        PREFERRED_JAVA_SQL_TYPE.put(Types.VARCHAR, "String");
        PREFERRED_JAVA_SQL_TYPE.put(Types.LONGVARCHAR, "String");
        PREFERRED_JAVA_SQL_TYPE.put(Types.BINARY, "byte[]");
        PREFERRED_JAVA_SQL_TYPE.put(Types.VARBINARY, "byte[]");
        PREFERRED_JAVA_SQL_TYPE.put(Types.LONGVARBINARY, "byte[]");
        PREFERRED_JAVA_SQL_TYPE.put(Types.DATE, "java.util.Date");
        PREFERRED_JAVA_SQL_TYPE.put(Types.TIME, "java.util.Date");
        PREFERRED_JAVA_SQL_TYPE.put(Types.TIMESTAMP, "java.util.Date");
        PREFERRED_JAVA_SQL_TYPE.put(Types.CLOB, "String");
        PREFERRED_JAVA_SQL_TYPE.put(Types.BLOB, "byte[]");
    }

    private static class IntStringMap extends HashMap<Integer, String> {

        private static final long serialVersionUID = -9082031514103682995L;


        public String getString(int i) {
            return get(new Integer(i));
        }


        public void put(int i, String s) {
            put(new Integer(i), s);
        }
    }

}
