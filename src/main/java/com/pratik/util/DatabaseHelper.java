package com.pratik.util;

import io.dropwizard.db.DataSourceFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseHelper
{
    private final DataSourceFactory dataSourceFactory;

    public DatabaseHelper (DataSourceFactory dataSourceFactory)
    {
        this.dataSourceFactory = dataSourceFactory;
    }

    public Connection getConnection () throws SQLException
    {
        String url = dataSourceFactory.getUrl();
        String user = dataSourceFactory.getUser();
        String password = dataSourceFactory.getPassword();

        return DriverManager.getConnection(url,user,password);
    }
}
