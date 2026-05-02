<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%@ page import="com.expensesplitter.model.Expense" %>

<!DOCTYPE html>
<html>
<head>
    <title>Dashboard</title>

    <meta charset="UTF-8">

  
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">

    
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
</head>

<body class="bg-light">

<div class="container mt-5">

    <h2 class="text-center fw-bold mb-4 text-dark">Dashboard</h2>

    <%
        List<Expense> expenses = (List<Expense>) request.getAttribute("expenses");
        Map<String, Double> balances = (Map<String, Double>) request.getAttribute("balances");
        Map<Integer, String> splitDisplay = (Map<Integer, String>) request.getAttribute("splitDisplay");
        List<String> settlements = (List<String>) request.getAttribute("settlements");
    %>


    <div class="card shadow-sm p-3 mb-4">

        <div class="d-flex justify-content-between">
            <h4 class="fw-bold">Expense Details</h4>

            <span class="fw-semibold">
                Total Expense: ₹
                <%
                double total = 0;
                if(expenses != null){
                    for(Expense e : expenses){
                        total += e.getAmount();
                    }
                }
                %>
                <%= String.format("%.2f", total) %>
            </span>
        </div>

        <table class="table table-bordered mt-3 align-middle">
            <thead class="table-light">
                <tr>
                    <th>Action</th>
                    <th>Description</th>
                    <th>Payer</th>
                    <th>Amount</th>
                    <th>Split Details</th>
                </tr>
            </thead>

            <tbody>

            <%
            if(expenses != null && expenses.size() > 0){
                for(Expense e : expenses){
            %>

            <tr>
                <td>✏️ &nbsp; 🗑️</td>
                <td><%= e.getTitle() %></td>
                <td><%= e.getPaidBy() %></td>
                <td>₹<%= String.format("%.2f", e.getAmount()) %></td>

                <td>
                    <pre style="margin:0; font-size:13px;">
<%= splitDisplay.get(e.getId()) %>
                    </pre>
                </td>
            </tr>

            <%
                }
            } else {
            %>

            <tr>
                <td colspan="5" class="text-center">No expenses found</td>
            </tr>

            <% } %>

            </tbody>
        </table>

    </div>

    <div class="card shadow-sm p-3 mb-4">
        <h4 class="fw-bold mb-3">Balances</h4>

        <table class="table table-hover align-middle">
            <thead class="table-light">
                <tr>
                    <th>Name</th>
                    <th>Balance (₹)</th>
                </tr>
            </thead>

            <tbody>

            <%
            if(balances != null && balances.size() > 0){
                for(String person : balances.keySet()){
                    double amt = balances.get(person);
            %>

            <tr>
                <td><b><%= person %></b></td>

                <td>
                    <% if(amt < 0){ %>
                        <span class="text-danger fw-bold">
                            ₹<%= String.format("%.2f", amt) %> (Pay)
                        </span>
                    <% } else { %>
                        <span class="text-success fw-bold">
                            ₹<%= String.format("%.2f", amt) %> (Receive)
                        </span>
                    <% } %>
                </td>
            </tr>

            <%
                }
            } else {
            %>

            <tr>
                <td colspan="2" class="text-center">No balance data</td>
            </tr>

            <% } %>

            </tbody>
        </table>
    </div>


   
    <div class="card shadow-sm p-3 mb-4">
        <h4 class="fw-bold mb-3">Settlement Details</h4>

        <ul class="list-group">

        <%
        if(settlements != null && settlements.size() > 0){
            for(String s : settlements){
        %>

        <li class="list-group-item">
            <%= s %>
        </li>

        <%
            }
        } else {
        %>

        <li class="list-group-item">No settlements yet</li>

        <% } %>

        </ul>
    </div>


    
    <div class="text-center">
        <a href="<%=request.getContextPath()%>/index.jsp" class="btn btn-primary px-4 py-2">
            ➕ Add New Expense
        </a>
    </div>

</div>

</body>
</html>