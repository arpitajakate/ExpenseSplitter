<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Expense Splitter</title>
    <meta charset="UTF-8">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">

    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">

    <style>
        .chip {
            display: inline-flex;
            align-items: center;
            padding: 6px 12px;
            margin: 4px;
            background: #ede9fe;
            border-radius: 20px;
            font-size: 14px;
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
            border-radius: 8px;
            min-height: 45px;
            background: #faf5ff;
        }
    </style>
</head>

<body class="bg-light">

<div class="container mt-5">

    <h2 class="text-center fw-bold mb-4 text-dark">Expense Splitter</h2>

    <div class="card p-4 shadow-sm">

        <form action="<%=request.getContextPath()%>/addExpense" method="post">

          
            <div class="mb-3">
                <label class="fw-semibold">Participants</label>

                <input type="text" id="nameInput" class="form-control"
                       placeholder="Type name and press comma">

                <div id="chips" class="mt-2"></div>

                <span class="badge bg-primary mt-2" id="count">
                    Participants: 0
                </span>

                <input type="hidden" name="participants" id="participants">
            </div>

            <div class="mb-3">
                <label class="fw-semibold">Description</label>
                <input type="text" name="description" class="form-control" required>
            </div>

            <div class="mb-3">
                <label class="fw-semibold">Amount</label>
                <input type="number" name="amount" class="form-control" required>
            </div>

       
            <div class="mb-3">
                <label class="fw-semibold">Payer</label>
                <select name="payer" id="payer" class="form-control" required>
                    <option value="">Select payer</option>
                </select>
            </div>

           
            <button type="submit" class="btn btn-primary w-100 mt-3">
                ➕ Add Expense
            </button>

        </form>

    </div>
</div>

<script>
let participants = [];

const input = document.getElementById("nameInput");
const chipsDiv = document.getElementById("chips");
const hidden = document.getElementById("participants");
const count = document.getElementById("count");
const payer = document.getElementById("payer");

input.addEventListener("keyup", function(e) {
    if (e.key === "," || e.key === "Enter") {
        let name = input.value.replace(",", "").trim();

        if (name !== "" && !participants.includes(name)) {
            participants.push(name);
            render();
        }

        input.value = "";
    }
});

function render() {
    chipsDiv.innerHTML = "";
    payer.innerHTML = '<option value="">Select payer</option>';

    participants.forEach((p, index) => {

        let chip = document.createElement("div");
        chip.className = "chip";
        chip.innerHTML = p + ' <span onclick="remove(' + index + ')">×</span>';
        chipsDiv.appendChild(chip);

        let option = document.createElement("option");
        option.value = p;
        option.text = p;
        payer.appendChild(option);
    });

    hidden.value = participants.join(",");
    count.innerText = "Participants: " + participants.length;
}

function remove(index) {
    participants.splice(index, 1);
    render();
}
</script>

</body>
</html>