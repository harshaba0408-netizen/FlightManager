package com.flight;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.*;
import java.sql.*;

@WebServlet("/sortPrice")
public class SortPriceServlet extends HttpServlet {

    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws IOException {

        try {
            Connection con = DBConnection.getConnection();
            Statement st = con.createStatement();

            ResultSet rs = st.executeQuery(
                "SELECT * FROM flights ORDER BY price");

            res.setContentType("application/json");
            res.setCharacterEncoding("UTF-8");
            PrintWriter out = res.getWriter();
            out.print("[");
            boolean first = true;
            while (rs.next()) {
                if (!first) out.print(",");
                out.print("{");
                out.print("\"flight_number\":\"" + rs.getString(1) + "\",");
                out.print("\"destination\":\"" + rs.getString(2) + "\",");
                out.print("\"departure_time\":\"" + rs.getString(3) + "\",");
                out.print("\"price\":" + rs.getDouble(4));
                out.print("}");
                first = false;
            }
            out.print("]");

        } catch (Exception e) {
            e.printStackTrace();
            res.setStatus(500);
            res.getWriter().print("{\"error\":\"Internal Server Error\"}");
        }
    }
}