package com.tjl.userTruthCircleDemo.jdbcutil;

import java.sql.*;

public class JdbcUtil {

    private static final String URL = "jdbc:mysql://localhost:3306/my_util_db";
    private static final String USER = "root";
    private static final String PASSWORD = "123456";

    public static Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static Statement getStatement() throws SQLException, ClassNotFoundException {
        return getConnection().createStatement();
    }

    public static void main(String[] args) throws Exception {
        //1.加载驱动程序
        Class.forName("com.mysql.cj.jdbc.Driver");
        //2. 获得数据库连接
        Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
        //3.操作数据库，实现增删改查
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM test");
        //如果有数据，rs.next()返回true
        while(rs.next()){
            System.out.println("id: " + rs.getString("id")+" value："+rs.getString("value"));
        }
    }
}