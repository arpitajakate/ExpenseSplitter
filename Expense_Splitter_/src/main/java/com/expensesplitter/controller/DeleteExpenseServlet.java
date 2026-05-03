package com.expensesplitter.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.expensesplitter.dao.DBConnection;

@WebServlet("/deleteExpense")
public class DeleteExpenseServlet extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {
			int id = Integer.parseInt(request.getParameter("id"));

			Connection conn = DBConnection.getConnection();

			PreparedStatement ps = conn.prepareStatement(
				"DELETE FROM expenses WHERE id=?"
			);

			ps.setInt(1, id);
			ps.executeUpdate();

			response.sendRedirect(request.getContextPath() + "/dashboard");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}