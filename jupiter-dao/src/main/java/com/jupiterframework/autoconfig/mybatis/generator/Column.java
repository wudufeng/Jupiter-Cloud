package com.jupiterframework.autoconfig.mybatis.generator;

public class Column {
    /** 列名 */
    private String columnName;

    /** 列名对应的java驼峰命名 */
    private String fieldName;

    /** 注释 */
    private String comment;

    /** 字段對应的java类型 */
    private String javaType;

    /** 字段对应的jdbc类型 */
    private String jdbcType;

    /** 是否主键 */
    private boolean primaryKey;

    private boolean autoincrement;


    public Column() {
    }


    public String getColumnName() {
        return columnName;
    }


    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }


    public String getFieldName() {
        return fieldName;
    }


    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }


    public String getComment() {
        return comment;
    }


    public void setComment(String comment) {
        this.comment = comment;
    }


    public String getJavaType() {
        return javaType;
    }


    public void setJavaType(String javaType) {
        this.javaType = javaType;
    }


    public String getJdbcType() {
        return jdbcType;
    }


    public void setJdbcType(String jdbcType) {
        this.jdbcType = jdbcType;
    }


    public boolean isPrimaryKey() {
        return primaryKey;
    }


    public void setPrimaryKey(boolean isPrimaryKey) {
        this.primaryKey = isPrimaryKey;
    }


    public boolean isAutoincrement() {
        return autoincrement;
    }


    public void setAutoincrement(boolean autoincrement) {
        this.autoincrement = autoincrement;
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((columnName == null) ? 0 : columnName.hashCode());
        return result;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Column other = (Column) obj;
        if (columnName == null) {
            if (other.columnName != null)
                return false;
        } else if (!columnName.equals(other.columnName))
            return false;
        return true;
    }


    @Override
    public String toString() {
        return "Column [columnName=" + columnName + ", fieldName=" + fieldName + ", comment=" + comment + ", javaType=" + javaType + ", jdbcType=" + jdbcType + ", isPrimaryKey="
                + primaryKey + "]";
    }

}
