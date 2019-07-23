package com.jupiterframework.mybatis;

public enum CustomSqlMethod {

    INSERT_LIST("insertList", "批量插入数据", "<script>INSERT INTO %s %s VALUES %s</script>");

    private final String method;
    private final String desc;
    private final String sql;


    CustomSqlMethod(final String method, final String desc, final String sql) {
        this.method = method;
        this.desc = desc;
        this.sql = sql;
    }


    public String getMethod() {
        return this.method;
    }


    public String getDesc() {
        return this.desc;
    }


    public String getSql() {
        return this.sql;
    }
}
