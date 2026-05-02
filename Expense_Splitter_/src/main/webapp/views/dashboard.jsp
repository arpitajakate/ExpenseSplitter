<%@ page import="java.util.*" %>
<%@ page import="com.expensesplitter.model.Expense" %>

<!DOCTYPE html>
<html>
<head>
    <title>Dashboard</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>

<body class="bg-light">

<div class="container mt-5">

    <h2 class="text-center text-success mb-4">Dashboard</h2>

    <!-- ================= EXPENSE LIST ================= -->
    <div class="card p-3 mb-4">
        <h4>All Expenses</h4>

        <table class="table table-bordered mt-3">
            <tr>
                <th>ID</th>
                <th>Description</th>
                <th>Amount</th>
                <th>Paid By</th>
            </tr>

            <%
            List<Expense> expenses = (List<Expense>) request.getAttribute("expenses");

            if(expenses != null && expenses.size() > 0){
                for(Expense e : expenses){
            %>

            <tr>
                <td><%= e.getId() %></td>
                <td><%= e.getTitle() %></td>
                <td>₹<%= e.getAmount() %></td>
                <td><%= e.getPaidBy() %></td>
            </tr>

            <%
                }
            } else {
            %>

            <tr>
                <td colspan="4" class="text-center">No expenses yet</td>
            </tr>

            <% } %>
        </table>
    </div>


    <!-- ================= BALANCES ================= -->
    <div class="card p-3 mb-4">
        <h4>Balances</h4>

        <ul class="list-group mt-3">

        <%
        Map<String, Double> balances = (Map<String, Double>) request.getAttribute("balances");

        if(balances != null && balances.size() > 0){
            for(String person : balances.keySet()){
                double amt = balances.get(person);
        %>

        <li class="list-group-item">
            <b><%= person %></b> :
            ₹<%= String.format("%.2f", amt) %>
        </li>

        <%
            }
        } else {
        %>

        <li class="list-group-item">No balance data</li>

        <% } %>

        </ul>
    </div>


    <!-- ================= SETTLEMENT ================= -->
    <div class="card p-3 mb-4">
        <h4>Who Owes Whom 🔥</h4>

        <ul class="list-group mt-3">

        <%
        List<String> settlements = (List<String>) request.getAttribute("settlements");

        if(settlements != null && settlements.size() > 0){
            for(String s : settlements){
        %>

        <li class="list-group-item"><%= s %></li>

        <%
            }
        } else {
        %>

        <li class="list-group-item">No settlements yet</li>

        <% } %>

        </ul>
    </div>


    <!-- BACK BUTTON -->
    <div class="text-center">
        <a href="<%=request.getContextPath()%>/index.jsp" class="btn btn-primary">
            ➕ Add New Expense
        </a>
    </div>

</div>

</body>
</html>