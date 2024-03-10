package com.example.lab2.database;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class JDBCConnectionWrapper extends DbConnection{

    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost:3306";
    private static final String DB_URL_TEST = "jdbc:mysql://localhost:3307";
    private static final String USER = "root";
    private static final String PASSWORD = "root";
    private static final int TIMEOUT = 1;

    public JDBCConnectionWrapper(String schema) {
        try {
            Class.forName(JDBC_DRIVER);
            if(schema.equals("/sd-basics")) {
                connection = DriverManager.getConnection(DB_URL + schema, USER, PASSWORD);
            } else {
                connection = DriverManager.getConnection(DB_URL_TEST + schema, USER, PASSWORD);
            }
            createTables();
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void createTables() throws SQLException {
        Statement statement = connection.createStatement();

        String sql = "CREATE TABLE IF NOT EXISTS book(" +
                " id int(11) NOT NULL AUTO_INCREMENT, " +
                " author varchar(500) NOT NULL," +
                " title varchar(500) NOT NULL," +
                " publishedDate datetime DEFAULT NULL," +
                " PRIMARY KEY (id)," +
                " UNIQUE KEY id_UNIQUE (id)" +
                ") ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;";
        statement.execute(sql);
    }

    @Override
    public boolean testConnection() throws SQLException {
        return connection.isValid(TIMEOUT);
    }
}
