package com.jupiterframework.autoconfig.mybatis.generator;

import java.util.ArrayList;
import java.util.List;


public class Table {
    /** 表名 */
    private String tableName;

    /** 对应的java名 , 表名采用驼峰形式 */
    private String className;

    /** 注释 */
    private String comment;

    private List<Column> columns = new ArrayList<>();


    public Table() {

    }


    public Table(String tableName, String comment) {
        super();
        this.tableName = tableName;
        this.comment = comment;
    }


    public String getTableName() {
        return tableName;
    }


    public void setTableName(String tableName) {
        this.tableName = tableName;
    }


    public String getClassName() {
        return className;
    }


    public void setClassName(String className) {
        this.className = className;
    }


    public String getComment() {
        return comment;
    }


    public void setComment(String comment) {
        this.comment = comment;
    }


    public List<Column> getColumns() {
        return columns;
    }


    public void setColumns(List<Column> columns) {
        this.columns = columns;
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((tableName == null) ? 0 : tableName.hashCode());
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
        Table other = (Table) obj;
        if (tableName == null) {
            if (other.tableName != null)
                return false;
        } else if (!tableName.equals(other.tableName))
            return false;
        return true;
    }


    @Override
    public String toString() {
        return "Table [tableName=" + tableName + ", className=" + className + ", comment=" + comment + ", columns=" + columns + "]";
    }

}
