package com.expensesplitter.controller;

import com.expensesplitter.dao.ExpenseDAO;
import com.expensesplitter.model.Expense;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.*;

@WebServlet("/viewExpenses")
public class ViewExpenseServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        ExpenseDAO dao = new ExpenseDAO();

        List<Expense> list = dao.getAllExpenses();
        List<String> settlements = dao.getSettlement(); // 🔥 ON AGAIN

        request.setAttribute("expenses", list);
        request.setAttribute("settlements", settlements);

        request.getRequestDispatcher("/views/dashboard.jsp")
                .forward(request, response);
    }
}