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

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            String desc = request.getParameter("description");
            double amount = Double.parseDouble(request.getParameter("amount"));
            String payer = request.getParameter("payer");

            String[] parts = request.getParameter("participants").split(",");

            List<String> participants = new ArrayList<>();
            for (String p : parts) {
                participants.add(p.trim());
            }

            String splitType = request.getParameter("splitType");

            Map<String, Double> map = new HashMap<>();

            for (String p : participants) {
                String val = request.getParameter("split_" + p);
                if (val != null && !val.isEmpty()) {
                    map.put(p, Double.parseDouble(val));
                }
            }

            Expense exp = new Expense(desc, amount, payer);

            new ExpenseDAO().addExpense(exp, participants, splitType, map);

            response.sendRedirect("viewExpenses");

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("views/index.jsp");
        }
    }
}