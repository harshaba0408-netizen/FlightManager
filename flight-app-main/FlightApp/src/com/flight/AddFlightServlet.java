package com.flight;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.*;
import java.sql.*;

@WebServlet("/add")
public class AddFlightServlet extends HttpServlet {

    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws IOException {

        try {
            String fn = req.getParameter("fno");
            String d = req.getParameter("dest");
            String t = req.getParameter("time");
            double p = Double.parseDouble(req.getParameter("price"));

            Connection con = DBConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(
                "INSERT INTO flights (flight_number, destination, departure_time, price) VALUES (?, ?, ?, ?)");

            ps.setString(1, fn);
            ps.setString(2, d);
            ps.setString(3, t);
            ps.setDouble(4, p);

            ps.executeUpdate();

            res.setContentType("application/json");
            res.setCharacterEncoding("UTF-8");
            res.getWriter().println("{\"status\":\"success\", \"message\":\"Flight Added Successfully!\"}");

        } catch (Exception e) {
            try (PrintWriter pw = new PrintWriter(new FileWriter("c:\\Users\\N E\\OneDrive\\Desktop\\AJAVA Project\\ajava\\servlet_error.log", true))) {
                e.printStackTrace(pw);
            } catch (IOException ie) {}
            
            e.printStackTrace();
            res.setStatus(500);
            res.setContentType("application/json");
            res.setCharacterEncoding("UTF-8");
            String errorMsg = e.getMessage() != null ? e.getMessage() : "Unknown Error";
            // Aggressive cleaning of control characters
            errorMsg = errorMsg.replaceAll("[\\x00-\\x1F]", " ").replace("\"", "\\\"");
            res.getWriter().println("{\"status\":\"error\", \"message\":\"" + errorMsg + "\"}");
        }
    }
}