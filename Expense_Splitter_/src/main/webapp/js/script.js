let participants = [];

const input = document.getElementById("nameInput");
const chipsDiv = document.getElementById("chips");
const count = document.getElementById("count");
const payerDropdown = document.getElementById("payer");
const form = document.querySelector("form");

// Add participant on comma or Enter
input.addEventListener("keyup", function (e) {

    if (e.key === "," || e.key === "Enter") {

        let name = input.value.replace(",", "").trim();

        if (name !== "" && !participants.includes(name)) {
            participants.push(name);
            refreshUI();
        }

        input.value = "";
    }
});

// Refresh UI
function refreshUI() {

    chipsDiv.innerHTML = "";
    payerDropdown.innerHTML = "<option value=''>Select payer</option>";

    participants.forEach(name => {

        // Chip UI
        let chip = document.createElement("div");
        chip.className = "chip";
        chip.innerHTML = name + " <span onclick='removeChip(\"" + name + "\")'>x</span>";
        chipsDiv.appendChild(chip);

        // Dropdown option
        let opt = document.createElement("option");
        opt.value = name;
        opt.text = name;
        payerDropdown.appendChild(opt);
    });

    count.innerText = "Participants: " + participants.length;
}

// Remove participant
function removeChip(name) {
    participants = participants.filter(p => p !== name);
    refreshUI();
}

// Submit validation
form.addEventListener("submit", function (e) {

    if (participants.length === 0) {
        alert("Add participants first!");
        e.preventDefault();
        return;
    }

    document.getElementById("participants").value = participants.join(",");

    console.log("Sending participants:", participants.join(","));
});