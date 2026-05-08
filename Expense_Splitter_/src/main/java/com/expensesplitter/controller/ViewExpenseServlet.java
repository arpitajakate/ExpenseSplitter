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

        String start = request.getParameter("start");
        String end = request.getParameter("end");

        List<Expense> expenses;

        if (start != null && end != null && !start.isEmpty() && !end.isEmpty()) {
            expenses = dao.getExpensesByDate(start, end);
        } else {
            expenses = dao.getAllExpenses();
        }

        Map<Integer, List<Split>> splitMap = dao.getSplits();

        // =========================
        // BALANCE CALCULATION
        // =========================
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

        // =========================
        // TOTAL SPLIT & PAID
        // =========================
        Map<String, Double> totalSplit = new HashMap<>();
        Map<String, Double> totalPaid = new HashMap<>();

        for (String p : people) {
            totalSplit.put(p, 0.0);
            totalPaid.put(p, 0.0);
        }

        for (Expense e : expenses) {

            totalPaid.put(e.getPaidBy(),
                    totalPaid.get(e.getPaidBy()) + e.getAmount());

            List<Split> splits = splitMap.get(e.getId());

            if (splits != null) {
                for (Split s : splits) {
                    totalSplit.put(s.getParticipant(),
                            totalSplit.get(s.getParticipant()) + s.getAmount());
                }
            }
        }

        Map<String, Double> finalBalance = new HashMap<>();

        for (String p : people) {
            finalBalance.put(p,
                    totalPaid.get(p) - totalSplit.get(p));
        }

        // =========================
        // SPLIT DISPLAY
        // =========================
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

        // =========================
        // ✅ SMART SETTLEMENT (MIN CASH FLOW)
        // =========================
        List<String> settlements = new ArrayList<>();

        List<Map.Entry<String, Double>> list = new ArrayList<>(balances.entrySet());

        // Sort balances
        list.sort(Map.Entry.comparingByValue());

        int i = 0;
        int j = list.size() - 1;

        while (i < j) {

            String debtor = list.get(i).getKey();
            double debit = list.get(i).getValue();

            String creditor = list.get(j).getKey();
            double credit = list.get(j).getValue();

            // skip settled
            if (Math.abs(debit) < 0.01) {
                i++;
                continue;
            }

            if (Math.abs(credit) < 0.01) {
                j--;
                continue;
            }

            double settleAmount = Math.min(-debit, credit);

            // ✅ formatted output
            settlements.add(
                debtor + " pays ₹" + String.format("%.2f", settleAmount) + " to " + creditor
            );

            // update balances
            list.get(i).setValue(debit + settleAmount);
            list.get(j).setValue(credit - settleAmount);
        }

        // =========================
        // SEND TO JSP
        // =========================
        request.setAttribute("expenses", expenses);
        request.setAttribute("splitDisplay", splitDisplay);
        request.setAttribute("settlements", settlements);

        request.setAttribute("totalSplit", totalSplit);
        request.setAttribute("totalPaid", totalPaid);
        request.setAttribute("finalBalance", finalBalance);

        request.getRequestDispatcher("/views/dashboard.jsp")
                .forward(request, response);
    }
}