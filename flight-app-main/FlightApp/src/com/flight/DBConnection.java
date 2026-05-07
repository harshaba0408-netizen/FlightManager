package com.flight;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

    public static Connection getConnection() throws Exception {

        Class.forName("org.postgresql.Driver");

        // Environment variables for Render, with local defaults
        String url = System.getenv("DB_URL");
        if (url == null) url = "jdbc:postgresql://ep-twilight-truth-am80d4nk.c-5.us-east-1.aws.neon.tech:5432/neondb?sslmode=require";
        
        String user = System.getenv("DB_USER");
        if (user == null) user = "neondb_owner";
        
        String password = System.getenv("DB_PASSWORD");
        if (password == null) password = "npg_9tYKlC3oZxqy";

        return DriverManager.getConnection(url, user, password);
    }
}