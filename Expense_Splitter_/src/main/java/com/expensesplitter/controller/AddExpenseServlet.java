package com.expensesplitter.controller;

import com.expensesplitter.dao.ExpenseDAO;
import com.expensesplitter.model.Expense;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.*;

@WebServlet("/addExpense")
public class AddExpenseServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            String desc = request.getParameter("description");
            double amount = Double.parseDouble(request.getParameter("amount"));
            String payer = request.getParameter("payer");
            String participantsStr = request.getParameter("participants");

            if (participantsStr == null || participantsStr.trim().isEmpty()) {
                response.getWriter().println("Participants missing!");
                return;
            }

            List<String> participants = new ArrayList<>();
            for (String p : participantsStr.split(",")) {
                if (!p.trim().isEmpty()) {
                    participants.add(p.trim());
                }
            }

            Expense exp = new Expense(desc, amount, payer);

            ExpenseDAO dao = new ExpenseDAO();
            dao.addExpense(exp, participants);

            response.sendRedirect(request.getContextPath() + "/viewExpenses");

        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("Error: " + e.getMessage());
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

      
        response.sendRedirect(request.getContextPath() + "/views/index.jsp");
    }
}