package com.expensesplitter.controller;

import com.expensesplitter.dao.ExpenseDAO;
import com.expensesplitter.model.Expense;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.*;

@WebServlet("/addExpense")
public class AddExpenseServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        try {
            String desc = request.getParameter("description");
            String amtStr = request.getParameter("amount");
            String payer = request.getParameter("payer");
            String participantsStr = request.getParameter("participants");

            double amount = Double.parseDouble(amtStr);

        
            List<String> participants = Arrays.asList(participantsStr.split(","));

            Expense exp = new Expense(desc, amount, payer);

            ExpenseDAO dao = new ExpenseDAO();
            dao.addExpense(exp, participants);

            response.sendRedirect("viewExpenses");

        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("ERROR: " + e.getMessage());
        }
    }
}