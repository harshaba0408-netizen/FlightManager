package com.flight;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.*;
import java.sql.*;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    // Handle GET → show login page
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws IOException, ServletException {
        res.sendRedirect("index.html");
    }

    // Handle POST → authenticate user
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws IOException, ServletException {

        String username = req.getParameter("username");
        String password = req.getParameter("password");

        try {
            Connection con = DBConnection.getConnection();

            // Ensure the users table exists
            Statement st = con.createStatement();
            st.execute(
                "CREATE TABLE IF NOT EXISTS users (" +
                "id SERIAL PRIMARY KEY, " +
                "username VARCHAR(100) UNIQUE NOT NULL, " +
                "password VARCHAR(100) NOT NULL)"
            );

            // Insert a default admin user if not present
            PreparedStatement insertAdmin = con.prepareStatement(
                "INSERT INTO users (username, password) VALUES (?, ?) ON CONFLICT (username) DO NOTHING"
            );
            insertAdmin.setString(1, "admin");
            insertAdmin.setString(2, "admin123");
            insertAdmin.executeUpdate();

            // Authenticate
            PreparedStatement ps = con.prepareStatement(
                "SELECT * FROM users WHERE username = ? AND password = ?"
            );
            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                // Valid login → create session and redirect to dashboard
                HttpSession session = req.getSession();
                session.setAttribute("loggedUser", username);
                res.sendRedirect("dashboard.html");
            } else {
                // Invalid → redirect back with error
                res.sendRedirect("index.html?error=1");
            }

            con.close();

        } catch (Exception e) {
            e.printStackTrace();
            res.sendRedirect("index.html?error=2");
        }
    }
}
