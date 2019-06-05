package com.jupiterframework.logging.sql.jdbcapi;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;
import java.util.logging.Logger;

import net.sf.log4jdbc.sql.jdbcapi.DriverSpy;


/**
 * spring.datasource.driverClassName设置为当前class, 则打印sql耗时
 * 
 * @author wudf
 *
 */
public class ElapsedLoggingDriver implements Driver {

    private static final String LOG4JDBC_URL_PREFIX = "jdbc:log4";

    private DriverSpy driverSpy;


    public ElapsedLoggingDriver() {
        this.driverSpy = new DriverSpy();
    }


    @Override
    public Connection connect(String url, Properties info) throws SQLException {
        return driverSpy.connect(LOG4JDBC_URL_PREFIX + url, info);
    }


    @Override
    public boolean acceptsURL(String url) throws SQLException {
        return driverSpy.acceptsURL(LOG4JDBC_URL_PREFIX + url);
    }


    @Override
    public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException {
        return driverSpy.getPropertyInfo(url, info);
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
