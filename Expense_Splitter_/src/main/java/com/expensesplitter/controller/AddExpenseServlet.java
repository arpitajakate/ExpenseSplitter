package com.expensesplitter.controller;

import com.expensesplitter.dao.ExpenseDAO;

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
            double amount = Double.parseDouble(request.getParameter("amount"));
            String payer = request.getParameter("payer");

            String[] arr = request.getParameter("participants").split(",");
            List<String> participants = Arrays.asList(arr);

            ExpenseDAO dao = new ExpenseDAO();
            dao.addExpense(desc, payer, amount, participants);

            response.sendRedirect("viewExpenses");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}