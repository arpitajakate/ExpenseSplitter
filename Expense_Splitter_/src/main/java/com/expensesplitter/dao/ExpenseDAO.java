package com.expensesplitter.dao;

import com.expensesplitter.model.Expense;
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

   
    public void addExpense(Expense exp, List<String> participants) {
        try (Connection conn = getConnection()) {

            String sql = "INSERT INTO expenses(title, amount, paid_by) VALUES(?,?,?)";
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            ps.setString(1, exp.getTitle());
            ps.setDouble(2, exp.getAmount());
            ps.setString(3, exp.getPaidBy());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            int expenseId = 0;
            if (rs.next()) expenseId = rs.getInt(1);

            double splitAmount = exp.getAmount() / participants.size();

            for (String p : participants) {
                PreparedStatement ps2 = conn.prepareStatement(
                        "INSERT INTO splits(expense_id, participant, amount) VALUES(?,?,?)"
                );
                ps2.setInt(1, expenseId);
                ps2.setString(2, p);
                ps2.setDouble(3, splitAmount);
                ps2.executeUpdate();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

   
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
                list.add(e);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

   
    public Map<String, Double> getBalances() {
        Map<String, Double> map = new HashMap<>();

        try (Connection conn = getConnection()) {

            ResultSet rs = conn.createStatement().executeQuery("SELECT participant, amount FROM splits");
            while (rs.next()) {
                String p = rs.getString("participant");
                double amt = rs.getDouble("amount");
                map.put(p, map.getOrDefault(p, 0.0) - amt);
            }

            ResultSet rs2 = conn.createStatement().executeQuery("SELECT paid_by, amount FROM expenses");
            while (rs2.next()) {
                String p = rs2.getString("paid_by");
                double amt = rs2.getDouble("amount");
                map.put(p, map.getOrDefault(p, 0.0) + amt);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return map;
    }

    
    public List<String> getSettlement() {

        Map<String, Double> balance = getBalances();
        List<String> result = new ArrayList<>();

        List<Map.Entry<String, Double>> list = new ArrayList<>(balance.entrySet());

        list.sort((a, b) -> Double.compare(a.getValue(), b.getValue()));

        int i = 0, j = list.size() - 1;

        while (i < j) {

            String debtor = list.get(i).getKey();
            double debit = list.get(i).getValue();

            String creditor = list.get(j).getKey();
            double credit = list.get(j).getValue();

            if (Math.abs(debit) < 0.01) {
                i++;
                continue;
            }

            if (Math.abs(credit) < 0.01) {
                j--;
                continue;
            }

            double settle = Math.min(-debit, credit);

            if (settle > 0) {

                result.add(debtor + " → ₹" + String.format("%.2f", settle) + " → " + creditor);

                list.get(i).setValue(debit + settle);
                list.get(j).setValue(credit - settle);
            } else {
                break;
            }
        }

        return result;
    }}