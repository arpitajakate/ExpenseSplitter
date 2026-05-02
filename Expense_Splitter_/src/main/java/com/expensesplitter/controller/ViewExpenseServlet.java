package com.expensesplitter.controller;

import com.expensesplitter.dao.ExpenseDAO;
import com.expensesplitter.model.Expense;
import com.expensesplitter.model.Split;

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

        // ================= FETCH DATA =================
        List<Expense> expenses = dao.getAllExpenses();
        Map<Integer, List<Split>> splitMap = dao.getSplits();

        // ================= BALANCE CALC =================
        Map<String, Double> balances = new HashMap<>();
        Set<String> people = new HashSet<>();

        for (Expense e : expenses) {
            people.add(e.getPaidBy());

            List<Split> splits = splitMap.get(e.getId());
            if (splits != null) {
                for (Split s : splits) {
                    people.add(s.getParticipant());
                }
            }
        }

        for (String p : people) {
            balances.put(p, 0.0);
        }

        for (Expense e : expenses) {

            // add paid amount
            balances.put(
                e.getPaidBy(),
                balances.get(e.getPaidBy()) + e.getAmount()
            );

            // subtract split
            List<Split> splits = splitMap.get(e.getId());

            if (splits != null) {
                for (Split s : splits) {
                    balances.put(
                        s.getParticipant(),
                        balances.get(s.getParticipant()) - s.getAmount()
                    );
                }
            }
        }

        // ================= SPLIT DISPLAY =================
        Map<Integer, String> splitDisplay = new HashMap<>();

        for (Integer expId : splitMap.keySet()) {

            List<Split> splits = splitMap.get(expId);

            StringBuilder sb = new StringBuilder();
            sb.append("{\n");

            for (int i = 0; i < splits.size(); i++) {

                Split s = splits.get(i);

                sb.append("\"")
                  .append(s.getParticipant())
                  .append("\": ")
                  .append(String.format("%.2f", s.getAmount()));

                if (i != splits.size() - 1) {
                    sb.append(",\n");
                }
            }

            sb.append("\n}");

            splitDisplay.put(expId, sb.toString());
        }

        // ================= SETTLEMENT =================
        List<String> settlements = new ArrayList<>();

        List<String> debtors = new ArrayList<>();
        List<String> creditors = new ArrayList<>();

        for (String p : balances.keySet()) {
            double amt = balances.get(p);

            if (amt < 0) debtors.add(p);
            else if (amt > 0) creditors.add(p);
        }

        int i = 0, j = 0;

        while (i < debtors.size() && j < creditors.size()) {

            String d = debtors.get(i);
            String c = creditors.get(j);

            double debt = -balances.get(d);
            double credit = balances.get(c);

            double amt = Math.min(debt, credit);

            settlements.add(
                d + " → ₹" + String.format("%.2f", amt) + " → " + c
            );

            balances.put(d, balances.get(d) + amt);
            balances.put(c, balances.get(c) - amt);

            if (Math.abs(balances.get(d)) < 0.01) i++;
            if (Math.abs(balances.get(c)) < 0.01) j++;
        }

        // ================= SEND TO JSP =================
        request.setAttribute("expenses", expenses);
        request.setAttribute("balances", balances);
        request.setAttribute("splitDisplay", splitDisplay);
        request.setAttribute("settlements", settlements);

        request.getRequestDispatcher("/views/dashboard.jsp").forward(request, response);
    }
}