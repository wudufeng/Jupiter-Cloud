package com.jupiterframework.autoconfig.mybatis.generator;

public enum DBType {
    MYSQL("jdbc:mysql://%s:%s/%s?useUnicode=true&characterEncoding=utf-8&connectTimeout=3000"),
    ORACLE("jdbc:oracle:thin:@%s:%s:%s"),
    DB2("jdbc:db2://%s:%s/%s");

    private String jdbcUrlTemplate;


    DBType(String jdbcUrlTemplate) {
        this.jdbcUrlTemplate = jdbcUrlTemplate;
    }


    public String getJdbcUrlTemplate() {
        return jdbcUrlTemplate;
    }
}