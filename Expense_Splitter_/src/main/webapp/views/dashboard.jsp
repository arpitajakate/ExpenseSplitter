<%@ page import="java.util.*, com.expensesplitter.model.Expense" %>

<!DOCTYPE html>
<html>
<head>
    <title>All Expenses</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/views/style.css">
</head>

<body>

<div class="container">

<h2>Expense Details</h2>

<div class="card">

<table>
<tr>
    <th>ID</th>
    <th>Description</th>
    <th>Payer</th>
    <th>Amount</th>
</tr>

<%
List<Expense> list = (List<Expense>) request.getAttribute("expenses");

double total = 0;

if(list != null && !list.isEmpty()){
    for(Expense e : list){
        total += e.getAmount();
%>
<tr>
    <td><%= e.getId() %></td>
    <td><%= e.getTitle() %></td>
    <td><%= e.getPaidBy() %></td>
    <td>₹ <%= e.getAmount() %></td>
</tr>
<%
    }
} else {
%>
<tr>
    <td colspan="4">No Data Found</td>
</tr>
<%
}
%>

</table>

</div>

<div class="card">
    <h3>Total Expense: ₹ <%= total %></h3>
</div>

<div class="card">
    <a href="<%=request.getContextPath()%>/views/index.jsp">Back</a>
</div>

</div>

</body>
</html>