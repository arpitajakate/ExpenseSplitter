package com.expensesplitter.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

import com.expensesplitter.dao.DBConnection;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/deleteExpense")
public class DeleteExpenseServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            String idStr = request.getParameter("id");

            if (idStr == null || idStr.isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/views/index.jsp");
                return;
            }

            int id = Integer.parseInt(idStr);

            Connection conn = DBConnection.getConnection();

            PreparedStatement ps = conn.prepareStatement(
                    "DELETE FROM expenses WHERE id=?"
            );

            ps.setInt(1, id);
            ps.executeUpdate();

            ps.close();
            conn.close();
            response.sendRedirect(request.getContextPath() + "/viewExpenses");

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/views/index.jsp");
        }
    }
}