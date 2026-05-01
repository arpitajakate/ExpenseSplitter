package com.expensesplitter.dao;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

    public static Connection getConnection() {
        Connection conn = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            String url = "jdbc:mysql://localhost:3306/expensesplitter";
            String user = "root";
            String pass = "root";

            conn = DriverManager.getConnection(url, user, pass);

            System.out.println("DB Connected ✅");

        } catch (Exception e) {
            e.printStackTrace();
        }

        return conn;
    }
}