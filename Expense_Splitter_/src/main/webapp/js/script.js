let participants = [];

const input = document.getElementById("nameInput");
const chipsDiv = document.getElementById("chips");
const count = document.getElementById("count");
const payerDropdown = document.getElementById("payer");
const form = document.querySelector("form");

// ✅ ENTER + COMMA support
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

// UI refresh
function refreshUI() {

    chipsDiv.innerHTML = "";
    payerDropdown.innerHTML = "<option value=''>Select payer</option>";

    participants.forEach(name => {

        // chip
        let chip = document.createElement("div");
        chip.className = "chip";
        chip.innerHTML = name + " <span onclick='removeChip(\"" + name + "\")'>x</span>";
        chipsDiv.appendChild(chip);

        // dropdown
        let opt = document.createElement("option");
        opt.value = name;
        opt.text = name;
        payerDropdown.appendChild(opt);
    });

    count.innerText = "Participants: " + participants.length;
}


function removeChip(name) {
    participants = participants.filter(p => p !== name);
    refreshUI();
}

form.addEventListener("submit", function (e) {

    if (participants.length === 0) {
        alert("Add participants first!");
        e.preventDefault();
        return;
    }

    let data = participants.join(",");
    document.getElementById("participants").value = data;

    console.log("Sending participants:", data); // 🔥 DEBUG
});