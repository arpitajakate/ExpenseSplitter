package com.expensesplitter.controller;

import com.expensesplitter.dao.ExpenseDAO;
import com.expensesplitter.model.Expense;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/addExpense")
public class AddExpenseServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            String desc = request.getParameter("description");
            String amountStr = request.getParameter("amount");
            String payer = request.getParameter("payer");
            String participantsStr = request.getParameter("participants");

            if (amountStr == null || payer == null || participantsStr == null) {
                response.sendRedirect(request.getContextPath() + "/views/index.jsp");
                return;
            }

            double amount = Double.parseDouble(amountStr);

            List<String> participants = new ArrayList<>();

            for (String p : participantsStr.split(",")) {
                if (p != null && !p.trim().isEmpty()) {
                    participants.add(p.trim());
                }
            }

            Expense exp = new Expense(desc, amount, payer);

            ExpenseDAO dao = new ExpenseDAO();
            dao.addExpense(exp, participants);

           
            response.sendRedirect(request.getContextPath() + "/viewExpenses");

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/views/index.jsp");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

       
        response.sendRedirect(request.getContextPath() + "/views/index.jsp");
    }
}