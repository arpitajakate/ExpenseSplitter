package com.expensesplitter.dao;

import com.expensesplitter.model.Expense;
import com.expensesplitter.model.Split;

import java.sql.*;
import java.util.*;

public class ExpenseDAO {

    private Connection getConnection() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/expensesplitter",
                "root",
                "root"
        );
    }

    // ✅ ADD EXPENSE
    public void addExpense(Expense exp, List<String> participants,
                           String splitType, Map<String, Double> inputValues) {

        try (Connection conn = getConnection()) {

            String sql = "INSERT INTO expenses(title, amount, paid_by, date) VALUES(?,?,?,CURDATE())";
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            ps.setString(1, exp.getTitle());
            ps.setDouble(2, exp.getAmount());
            ps.setString(3, exp.getPaidBy());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            int expenseId = 0;

            if (rs.next()) expenseId = rs.getInt(1);

            String splitSql = "INSERT INTO splits(expense_id, participant, amount) VALUES(?,?,?)";
            PreparedStatement ps2 = conn.prepareStatement(splitSql);

            for (String p : participants) {

                double amount = 0;

                if ("equal".equals(splitType)) {
                    amount = exp.getAmount() / participants.size();
                } 
                else if ("exact".equals(splitType)) {
                    amount = inputValues.getOrDefault(p, 0.0);
                } 
                else if ("percentage".equals(splitType)) {
                    double percent = inputValues.getOrDefault(p, 0.0);
                    amount = (percent / 100) * exp.getAmount();
                }

                ps2.setInt(1, expenseId);
                ps2.setString(2, p);
                ps2.setDouble(3, amount);
                ps2.executeUpdate();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ✅ GET ALL EXPENSES
    public List<Expense> getAllExpenses() {

        List<Expense> list = new ArrayList<>();

        try (Connection conn = getConnection()) {

            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM expenses");

            while (rs.next()) {
                Expense e = new Expense();

                e.setId(rs.getInt("id"));
                e.setTitle(rs.getString("title"));
                e.setAmount(rs.getDouble("amount"));
                e.setPaidBy(rs.getString("paid_by"));
                e.setDate(rs.getDate("date"));   // ✅ FIX

                list.add(e);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // ✅ FILTER BY DATE
    public List<Expense> getExpensesByDate(String start, String end) {

        List<Expense> list = new ArrayList<>();

        try (Connection conn = getConnection()) {

            String sql = "SELECT * FROM expenses WHERE date BETWEEN ? AND ?";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, start);
            ps.setString(2, end);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Expense e = new Expense();

                e.setId(rs.getInt("id"));
                e.setTitle(rs.getString("title"));
                e.setAmount(rs.getDouble("amount"));
                e.setPaidBy(rs.getString("paid_by"));
                e.setDate(rs.getDate("date"));   // ✅ FIX

                list.add(e);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // ✅ GET SPLITS
    public Map<Integer, List<Split>> getSplits() {

        Map<Integer, List<Split>> map = new HashMap<>();

        try (Connection conn = getConnection()) {

            String sql = "SELECT * FROM splits";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                int expenseId = rs.getInt("expense_id");
                String name = rs.getString("participant");
                double amt = rs.getDouble("amount");

                Split s = new Split(expenseId, name, amt);

                map.computeIfAbsent(expenseId, k -> new ArrayList<>()).add(s);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return map;
    }
}