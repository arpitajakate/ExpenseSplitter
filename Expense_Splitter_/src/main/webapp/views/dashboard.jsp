<%@ page import="java.util.*,com.expensesplitter.model.Expense" %>

<!DOCTYPE html>
<html>
<head>
    <title>Dashboard</title>

   
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>

<body class="bg-light">

<div class="container mt-4">

    <h2 class="mb-4">Expense Dashboard</h2>

    
    <div class="card mb-4 p-3">
        <form action="addExpense" method="post">

            <div class="row">
                <div class="col-md-3">
                    <input type="text" name="description" class="form-control" placeholder="Description" required>
                </div>

                <div class="col-md-2">
                    <input type="number" name="amount" class="form-control" placeholder="Amount" required>
                </div>

                <div class="col-md-2">
                    <input type="text" name="payer" class="form-control" placeholder="Paid By" required>
                </div>

                <div class="col-md-3">
                    <input type="text" name="participants" class="form-control"
                           placeholder="a,b,c (comma separated)" required>
                </div>

                <div class="col-md-2">
                    <button class="btn btn-primary w-100">Add</button>
                </div>
            </div>

        </form>
    </div>

    
    <div class="card p-3">
        <h4>Expense List</h4>

        <table class="table table-bordered mt-3">
            <thead class="table-dark">
                <tr>
                    <th>ID</th>
                    <th>Description</th>
                    <th>Amount</th>
                    <th>Payer</th>
                </tr>
            </thead>

            <tbody>

            <%
                List<Expense> list = (List<Expense>) request.getAttribute("expenses");

                if (list != null && !list.isEmpty()) {
                    for (Expense e : list) {
            %>

                <tr>
                    <td><%= e.getId() %></td>
                    <td><%= e.getTitle() %></td>
                    <td>₹ <%= e.getAmount() %></td>
                    <td><%= e.getPaidBy() %></td>
                </tr>

            <%
                    }
                } else {
            %>

                <tr>
                    <td colspan="4" class="text-center text-danger">No Data Found</td>
                </tr>

            <%
                }
            %>

            </tbody>
        </table>
    </div>

</div>

</body>
</html>