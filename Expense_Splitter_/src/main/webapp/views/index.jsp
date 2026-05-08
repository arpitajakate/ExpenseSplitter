<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Expense Splitter</title>
    <meta charset="UTF-8">

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css" rel="stylesheet">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
</head>

<body class="bg-light">

<div class="container mt-5">

    <h2 class="text-center fw-bold mb-4 text-dark">Expense Splitter</h2>

    <div class="card p-4">

        <form action="<%=request.getContextPath()%>/addExpense" method="post">

            <!-- Participants -->
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

            <!-- Description -->
            <div class="mb-3">
                <label class="fw-semibold">Description</label>
                <input type="text" name="description" class="form-control" required>
            </div>

            <!-- Amount -->
            <div class="mb-3">
                <label class="fw-semibold">Amount</label>
                <input type="number" name="amount" class="form-control" required>
            </div>

            <!-- Split Type -->
            <div class="mb-3">
                <label class="fw-semibold">Split Type</label>
                <select name="splitType" id="splitType" class="form-control">
                    <option value="equal">Equal</option>
                    <option value="exact">Exact</option>
                    <option value="percentage">Percentage</option>
                </select>
            </div>

            <!-- Dynamic Inputs -->
            <div id="splitInputs" class="mb-3"></div>

            <!-- Payer -->
            <div class="mb-3">
                <label class="fw-semibold">Payer</label>
                <select name="payer" id="payer" class="form-control" required>
                    <option value="">Select payer</option>
                </select>
            </div>

            <!-- Submit -->
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
const splitType = document.getElementById("splitType");
const splitInputsDiv = document.getElementById("splitInputs");

// Add participants
input.addEventListener("keyup", function (e) {
    if (e.key === "," || e.key === "Enter") {

        let name = input.value.replace(",", "").trim();

        if (name !== "" && !participants.includes(name)) {
            participants.push(name);
            render();
            renderSplitInputs();
        }

        input.value = "";
    }
});

// Render UI
function render() {
    chipsDiv.innerHTML = "";
    payer.innerHTML = '<option value="">Select payer</option>';

    participants.forEach((p, index) => {

        let chip = document.createElement("div");
        chip.className = "chip";
        chip.innerHTML = p + ' <span onclick="removeItem(' + index + ')">×</span>';
        chipsDiv.appendChild(chip);

        let option = document.createElement("option");
        option.value = p;
        option.text = p;
        payer.appendChild(option);
    });

    hidden.value = participants.join(",");
    count.innerText = "Participants: " + participants.length;
}

// Remove participant
function removeItem(index) {
    participants.splice(index, 1);
    render();
    renderSplitInputs();
}

// Split type change
splitType.addEventListener("change", renderSplitInputs);

// Render split inputs
function renderSplitInputs() {
    splitInputsDiv.innerHTML = "";

    if (splitType.value === "equal") return;

    participants.forEach(p => {
        let input = document.createElement("input");
        input.className = "form-control mt-2";
        input.name = "split_" + p;
        input.required = true;

        if (splitType.value === "exact") {
            input.placeholder = p + " amount";
        } else {
            input.placeholder = p + " %";
        }

        splitInputsDiv.appendChild(input);
    });
}
</script>

</body>
</html>