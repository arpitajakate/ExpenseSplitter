package com.expensesplitter.dao;

import com.expensesplitter.model.Expense;
import java.sql.*;
import java.util.*;

public class ExpenseDAO {

    // ADD EXPENSE + SPLIT
    public void addExpense(Expense exp, List<String> participants) {

        try {
            Connection conn = DBConnection.getConnection();

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

            // 🔥 Equal Split Logic
            double share = exp.getAmount() / participants.size();

            String splitSql = "INSERT INTO splits(expense_id, participant, amount) VALUES(?,?,?)";
            PreparedStatement ps2 = conn.prepareStatement(splitSql);

            for (String p : participants) {
                ps2.setInt(1, expenseId);
                ps2.setString(2, p);
                ps2.setDouble(3, share);
                ps2.executeUpdate();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // GET ALL EXPENSES
    public List<Expense> getAllExpenses() {

        List<Expense> list = new ArrayList<>();

        try {
            Connection conn = DBConnection.getConnection();

            String sql = "SELECT * FROM expenses";
            PreparedStatement ps = conn.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

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

	public void addExpense(String desc, String payer, double amount, List<String> participants) {
		// TODO Auto-generated method stub
		
	}
}