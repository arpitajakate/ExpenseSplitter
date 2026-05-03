<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*"%>
<%@ page import="com.expensesplitter.model.Expense"%>

<!DOCTYPE html>
<html>
<head>
<title>Dashboard</title>
<meta charset="UTF-8">

<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
<link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css" rel="stylesheet">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/style.css">
</head>

<body class="bg-light">

<div class="container mt-5">

	<h2 class="text-center fw-bold mb-4 text-dark">Dashboard</h2>

	<%
	List<Expense> expenses = (List<Expense>) request.getAttribute("expenses");
	Map<Integer, String> splitDisplay = (Map<Integer, String>) request.getAttribute("splitDisplay");
	List<String> settlements = (List<String>) request.getAttribute("settlements");

	Map<String, Double> totalSplit = (Map<String, Double>) request.getAttribute("totalSplit");
	Map<String, Double> totalPaid = (Map<String, Double>) request.getAttribute("totalPaid");
	Map<String, Double> finalBalance = (Map<String, Double>) request.getAttribute("finalBalance");
	%>


	<div class="card shadow-sm p-3 mb-4">

		<div class="d-flex justify-content-between">
			<h4 class="fw-bold">Expense Details</h4>

			<span class="fw-semibold">
				<i class="bi bi-people-fill me-2"></i>
				Total Expense: ₹
				<%
				double total = 0;
				if (expenses != null) {
					for (Expense e : expenses) {
						total += e.getAmount();
					}
				}
				%>
				<%=String.format("%.2f", total)%>
			</span>
		</div>

		<table class="table table-bordered mt-3 align-middle">
			<thead>
				<tr>
					<th>Action</th>
					<th>Description</th>
					<th>Payer</th>
					<th>Amount</th>
					<th>Split</th>
				</tr>
			</thead>

			<tbody>

			<%
			if (expenses != null && !expenses.isEmpty()) {
				for (Expense e : expenses) {
			%>

			<tr>

			
				<td>

				
					<a href="<%=request.getContextPath()%>/editExpense?id=<%=e.getId()%>"
   class="text-primary me-2">
   <i class="bi bi-pencil-square"></i>
</a>

				
					<a href="<%=request.getContextPath()%>/deleteExpense?id=<%=e.getId()%>"
   class="text-danger"
   onclick="return confirm('Delete this expense?');">
   <i class="bi bi-trash"></i>
</a>
				</td>

				<td><%=e.getTitle()%></td>
				<td><%=e.getPaidBy()%></td>
				<td>₹<%=String.format("%.2f", e.getAmount())%></td>
				<td><pre><%=splitDisplay != null ? splitDisplay.get(e.getId()) : ""%></pre></td>

			</tr>

			<%
				}
			} else {
			%>

			<tr>
				<td colspan="5" class="text-center">No data</td>
			</tr>

			<%
			}
			%>

			</tbody>
		</table>

	</div>


	<div class="card shadow-sm p-3 mb-4">
		<h4 class="fw-bold mb-3">Balances</h4>

		<table class="table table-hover">
			<thead>
				<tr>
					<th>Name</th>
					<th>Split</th>
					<th>Paid</th>
					<th>Balance</th>
				</tr>
			</thead>

			<tbody>

			<%
			if (totalSplit != null) {
				for (String person : totalSplit.keySet()) {
			%>

			<tr>
				<td><b><%=person%></b></td>
				<td>₹<%=String.format("%.2f", totalSplit.get(person))%></td>
				<td>₹<%=String.format("%.2f", totalPaid.get(person))%></td>
				<td>
					<%
					double amt = finalBalance.get(person);
					if (amt < 0) {
					%>
						<span class="text-danger">₹<%=String.format("%.2f", amt)%></span>
					<%
					} else {
					%>
						<span class="text-success">₹<%=String.format("%.2f", amt)%></span>
					<%
					}
					%>
				</td>
			</tr>

			<%
				}
			}
			%>

			</tbody>
		</table>
	</div>


	<div class="card shadow-sm p-3 mb-4">
		<h4 class="fw-bold">Settlement</h4>

		<ul class="list-group">
		<%
		if (settlements != null) {
			for (String s : settlements) {
		%>
			<li class="list-group-item"><%=s%></li>
		<%
			}
		}
		%>
		</ul>
	</div>


	<div class="text-center mb-4">
		<a href="<%=request.getContextPath()%>/views/index.jsp"
		   class="btn btn-primary px-4 py-2">
			➕ Add New Expense
		</a>
	</div>

</div>

</body>
</html>