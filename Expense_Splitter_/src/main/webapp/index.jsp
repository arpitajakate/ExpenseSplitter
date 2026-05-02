<!DOCTYPE html>
<html>
<head>
    <title>Expense Splitter</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/style.css">
</head>

<body>

<h2>Expense Splitter</h2>

<form action="<%=request.getContextPath()%>/addExpense" method="post">

    Names (comma separated):<br>
    <input type="text" id="names" placeholder="A,B,C"><br><br>

    Description:<br>
    <input type="text" name="description" required><br><br>

    Payer:<br>
    <select name="payer" id="payer"></select><br><br>

    Amount:<br>
    <input type="number" name="amount" required><br><br>

    <input type="hidden" name="participants" id="participants">

    <button type="submit">Add Expense</button>

</form>

<br>

<!-- FIXED LINK -->
<a href="<%=request.getContextPath()%>/viewExpenses">View Expenses</a>

<script>
document.getElementById("names").addEventListener("input", function(){

    let arr = this.value.split(",");
    let payer = document.getElementById("payer");

    payer.innerHTML = "";

    let cleanArr = [];

    arr.forEach(p => {
        let name = p.trim();
        if(name !== ""){
            cleanArr.push(name);

            let opt = document.createElement("option");
            opt.value = name;
            opt.innerText = name;
            payer.appendChild(opt);
        }
    });

    document.getElementById("participants").value = cleanArr.join(",");
});
</script>

</body>
</html>