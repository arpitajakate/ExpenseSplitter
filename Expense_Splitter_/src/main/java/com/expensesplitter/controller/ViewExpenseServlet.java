package com.expensesplitter.controller;

import com.expensesplitter.dao.ExpenseDAO;
import com.expensesplitter.model.Expense;
import com.expensesplitter.model.Split;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.*;

@WebServlet("/viewExpenses")
public class ViewExpenseServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        ExpenseDAO dao = new ExpenseDAO();

        List<Expense> expenses = dao.getAllExpenses();
        Map<Integer, List<Split>> splitMap = dao.getSplits();

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

            balances.put(e.getPaidBy(),
                    balances.get(e.getPaidBy()) + e.getAmount());

            List<Split> splits = splitMap.get(e.getId());

            if (splits != null) {
                for (Split s : splits) {
                    balances.put(s.getParticipant(),
                            balances.get(s.getParticipant()) - s.getAmount());
                }
            }
        }

        Map<String, Double> totalSplit = new HashMap<>();
        Map<String, Double> totalPaid = new HashMap<>();
        Map<String, Integer> txnCount = new HashMap<>();
        Map<String, Integer> payCount = new HashMap<>();

        for (String p : people) {
            totalSplit.put(p, 0.0);
            totalPaid.put(p, 0.0);
            txnCount.put(p, 0);
            payCount.put(p, 0);
        }

        for (Expense e : expenses) {

            totalPaid.put(e.getPaidBy(),
                    totalPaid.get(e.getPaidBy()) + e.getAmount());

            payCount.put(e.getPaidBy(),
                    payCount.get(e.getPaidBy()) + 1);

            List<Split> splits = splitMap.get(e.getId());

            if (splits != null) {
                for (Split s : splits) {

                    totalSplit.put(s.getParticipant(),
                            totalSplit.get(s.getParticipant()) + s.getAmount());

                    txnCount.put(s.getParticipant(),
                            txnCount.get(s.getParticipant()) + 1);
                }
            }
        }

        Map<String, Double> finalBalance = new HashMap<>();

        for (String p : people) {
            finalBalance.put(p,
                    totalPaid.get(p) - totalSplit.get(p));
        }

        Map<Integer, String> splitDisplay = new HashMap<>();

        for (Integer expId : splitMap.keySet()) {

            List<Split> splits = splitMap.get(expId);
            StringBuilder sb = new StringBuilder();

            sb.append("{");

            for (int i = 0; i < splits.size(); i++) {
                Split s = splits.get(i);

                sb.append(s.getParticipant())
                  .append(": ")
                  .append(String.format("%.2f", s.getAmount()));

                if (i != splits.size() - 1) sb.append(", ");
            }

            sb.append("}");

            splitDisplay.put(expId, sb.toString());
        }

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

            settlements.add(d + " → ₹" + amt + " → " + c);

            balances.put(d, balances.get(d) + amt);
            balances.put(c, balances.get(c) - amt);

            if (Math.abs(balances.get(d)) < 0.01) i++;
            if (Math.abs(balances.get(c)) < 0.01) j++;
        }

        request.setAttribute("expenses", expenses);
        request.setAttribute("balances", balances);
        request.setAttribute("splitDisplay", splitDisplay);
        request.setAttribute("settlements", settlements);

        request.setAttribute("totalSplit", totalSplit);
        request.setAttribute("totalPaid", totalPaid);
        request.setAttribute("txnCount", txnCount);
        request.setAttribute("payCount", payCount);
        request.setAttribute("finalBalance", finalBalance);

        request.getRequestDispatcher("/views/dashboard.jsp")
               .forward(request, response);
    }
}