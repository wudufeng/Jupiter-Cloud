package com.jupiterframework.mybatis;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;
import java.util.logging.Logger;


/**
 * spring.datasource.driverClassName设置为当前class, 则打印sql耗时
 * 
 * @author wudf
 *
 */
public class ElapsedLoggingDriver implements Driver {

    private static final String LOG4JDBC_URL_PREFIX = "jdbc:p6spy";

    private com.p6spy.engine.spy.P6SpyDriver driverSpy;


    public ElapsedLoggingDriver() {
        this.driverSpy = new com.p6spy.engine.spy.P6SpyDriver();
    }


    @Override
    public Connection connect(String url, Properties info) throws SQLException {
        return driverSpy.connect(url.replaceFirst("jdbc", LOG4JDBC_URL_PREFIX), info);
    }


    @Override
    public boolean acceptsURL(String url) throws SQLException {
        return driverSpy.acceptsURL(url.replaceFirst("jdbc", LOG4JDBC_URL_PREFIX));
    }


    @Override
    public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException {
        return driverSpy.getPropertyInfo(url.replaceFirst("jdbc", LOG4JDBC_URL_PREFIX), info);
    }


    @Override
    public int getMajorVersion() {

        return driverSpy.getMajorVersion();
    }


    @Override
    public int getMinorVersion() {
        return driverSpy.getMinorVersion();
    }


    @Override
    public boolean jdbcCompliant() {
        return driverSpy.jdbcCompliant();
    }


    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return driverSpy.getParentLogger();
    }

}
