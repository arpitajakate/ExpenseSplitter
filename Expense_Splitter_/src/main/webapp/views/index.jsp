<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Expense Splitter</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">

    <style>
        .chip {
            display: inline-flex;
            align-items: center;
            padding: 6px 12px;
            margin: 4px;
            background: #e0e0e0;
            border-radius: 20px;
        }

        .chip span {
            margin-left: 8px;
            cursor: pointer;
            color: red;
            font-weight: bold;
        }

        #chips {
            border: 1px solid #ccc;
            padding: 10px;
            border-radius: 6px;
            min-height: 45px;
        }
    </style>
</head>

<body class="bg-light">

<div class="container mt-5">
    <h2 class="text-center text-primary mb-4">Expense Splitter</h2>

    <div class="card p-4 shadow">

        <form action="<%=request.getContextPath()%>/addExpense" method="post" onsubmit="return prepareData()">

         
            <div class="mb-3">
                <label>Names</label>
                <input type="text" id="nameInput" class="form-control"
                       placeholder="Type name and press comma">

                <div id="chips"></div>

                <span class="badge bg-primary mt-2" id="count">Participants: 0</span>

                <input type="hidden" name="participants" id="participants">
            </div>

            <div class="mb-3">
                <label>Description</label>
                <input type="text" name="description" class="form-control" required>
            </div>

         
            <div class="mb-3">
                <label>Amount</label>
                <input type="number" name="amount" class="form-control" required>
            </div>

        
            <div class="mb-3">
                <label>Payer</label>
                <select name="payer" id="payer" class="form-control" required>
                    <option value="">Select payer</option>
                </select>
            </div>

            <button class="btn btn-success w-100 mt-3">Add Expense</button>

        </form>

    </div>
</div>

<script src="<%=request.getContextPath()%>/js/script.js"></script>

</body>
</html>