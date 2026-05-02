package com.expensesplitter.dao;

import com.expensesplitter.model.Expense;
import java.sql.*;
import java.util.*;

public class ExpenseDAO {

    Connection conn;

    public ExpenseDAO() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/expensesplitter",
                "root",
                "root"
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
  
    public void addExpense(Expense exp, List<String> participants) {
        try {
            String sql = "INSERT INTO expenses(title, amount, paid_by) VALUES(?,?,?)";
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            ps.setString(1, exp.getTitle());
            ps.setDouble(2, exp.getAmount());
            ps.setString(3, exp.getPaidBy());

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            int expenseId = 0;

            if (rs.next()) {
                expenseId = rs.getInt(1);
            }

            double splitAmount = exp.getAmount() / participants.size();

            String q2 = "INSERT INTO splits(expense_id, participant, amount) VALUES (?,?,?)";
            PreparedStatement ps2 = conn.prepareStatement(q2);

            for (String p : participants) {
                ps2.setInt(1, expenseId);
                ps2.setString(2, p.trim()); // IMPORTANT
                ps2.setDouble(3, splitAmount);
                ps2.addBatch();
            }

            ps2.executeBatch();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

  
    public List<Expense> getAllExpenses() {
        List<Expense> list = new ArrayList<>();

        try {
            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM expenses");

            while (rs.next()) {
                Expense e = new Expense();
                e.setId(rs.getInt("id"));
                e.setTitle(rs.getString("title"));
                e.setAmount(rs.getDouble("amount"));
                e.setPaidBy(rs.getString("paid_by"));
                list.add(e);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}