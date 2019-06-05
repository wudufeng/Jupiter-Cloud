package com.jupiterframework.autoconfig.mybatis.generator;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.JDBCType;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.AllArgsConstructor;


@AllArgsConstructor
public class DatabaseMetaDataProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseMetaData.class);

    private DataSource dataSource;


    /**
     * 获取指定ID的数据库下所有的表信息
     * 
     * @param dataSourceId 数据库ID
     * @param tableNames 可不传，支持%匹配
     * @return table
     */
    public List<Table> getTables(String... tableNames) {

        Connection conn = this.getConnection();

        if (conn == null)
            return new ArrayList<>();

        try {
            String schema = conn.getSchema();
            DatabaseMetaData dmd = conn.getMetaData();

            if (tableNames == null || tableNames.length == 0) {
                tableNames = new String[] { null };
            }

            // 获取所有表信息
            Map<String, Table> tables = new HashMap<>();
            for (String tablePattern : tableNames) {
                ResultSet tableRs = dmd.getTables(null, schema, tablePattern, new String[] { "TABLE" });
                while (tableRs.next()) {
                    String tableName = tableRs.getString("TABLE_NAME");
                    String remarks = tableRs.getString("REMARKS");

                    Table table = new Table(tableName, remarks);
                    table.setClassName(ConverterUtil.translateClassName(tableName));

                    tables.put(tableName, table);
                }
                tableRs.close();
            }

            // 获取所有字段信息
            for (String tablePattern : tableNames) {
                ResultSet columnRs = dmd.getColumns(null, schema, tablePattern, null);
                while (columnRs.next()) {
                    String tableName = columnRs.getString("TABLE_NAME");
                    String columnName = columnRs.getString("COLUMN_NAME");

                    Column column = new Column();
                    column.setColumnName(columnName);
                    column.setFieldName(ConverterUtil.translateFieldName(columnName));
                    column.setJdbcType(JDBCType.valueOf(columnRs.getInt("DATA_TYPE")).getName());
                    column.setJavaType(
                        DatabaseDataTypesUtil.getPreferredJavaType(columnRs.getInt("DATA_TYPE"), columnRs.getInt("COLUMN_SIZE"), columnRs.getString("DECIMAL_DIGITS")));
                    column.setComment(columnRs.getString("REMARKS"));
                    column.setAutoincrement("YES".equals(columnRs.getString("IS_AUTOINCREMENT")));
                    tables.get(tableName).getColumns().add(column);
                }
                columnRs.close();
            }

            // 获取表的主键信息
            for (String tableName : tables.keySet()) {
                ResultSet pkRs = dmd.getPrimaryKeys(null, schema, tableName);// 必须指定table
                while (pkRs.next()) {
                    String columnName = pkRs.getString("COLUMN_NAME");
                    for (Column col : tables.get(tableName).getColumns()) {
                        if (col.getColumnName().equalsIgnoreCase(columnName)) {
                            col.setPrimaryKey(true);
                            break;
                        }
                    }
                }
                pkRs.close();
            }

            List<Table> list = new ArrayList<>(tables.size());
            list.addAll(tables.values());
            return list;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                }
            }
        }
    }


    private Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            LOGGER.error("", e);
            return null;
        }
    }

}
