const API_URL = "http://localhost:8080";

const state = {
    employees: [],
    shifts: []
};

document.querySelectorAll(".nav-button").forEach((button) => {
    button.addEventListener("click", () => showPage(button.dataset.page));
});

document.querySelectorAll(".cancel-button").forEach((button) => {
    button.addEventListener("click", () => button.closest("form").classList.add("hidden"));
});

document.getElementById("add-employee-button").addEventListener("click", () => {
    resetForm("employee-form");
    document.getElementById("employee-form").classList.remove("hidden");
});

document.getElementById("add-shift-button").addEventListener("click", () => {
    resetForm("shift-form");
    document.getElementById("shift-form").classList.remove("hidden");
});

document.getElementById("add-roster-button").addEventListener("click", () => {
    fillSelects();
    document.getElementById("roster-form").classList.remove("hidden");
});

document.getElementById("add-swap-button").addEventListener("click", () => {
    fillSelects();
    document.getElementById("swap-form").classList.remove("hidden");
});

document.getElementById("employee-form").addEventListener("submit", saveEmployee);
document.getElementById("shift-form").addEventListener("submit", saveShift);
document.getElementById("roster-form").addEventListener("submit", saveRoster);
document.getElementById("swap-form").addEventListener("submit", saveSwapRequest);

function showPage(pageId) {
    document.querySelectorAll(".page").forEach((page) => page.classList.add("hidden"));
    document.getElementById(pageId).classList.remove("hidden");
    document.querySelectorAll(".nav-button").forEach((button) => {
        button.classList.toggle("active", button.dataset.page === pageId);
    });
    loadPageData(pageId);
}

async function request(path, options = {}) {
    const response = await fetch(API_URL + path, {
        headers: { "Content-Type": "application/json" },
        ...options
    });
    if (!response.ok) {
        const error = new Error("Request failed");
        error.status = response.status;
        throw error;
    }
    return response.status === 204 ? null : response.json();
}

async function loadPageData(pageId) {
    clearMessage();
    try {
        if (pageId === "employees") {
            state.employees = await request("/employees");
            renderEmployees();
        } else if (pageId === "shifts") {
            state.shifts = await request("/shifts");
            renderShifts();
        } else if (pageId === "roster") {
            await loadEmployeesAndShifts();
            renderRoster(await request("/rosters"));
        } else {
            await loadEmployeesAndShifts();
            renderSwapRequests(await request("/swap-requests"));
        }
    } catch (error) {
        showMessage("Could not load data. Check that the Spring Boot server is running.");
    }
}

async function loadEmployeesAndShifts() {
    const results = await Promise.all([request("/employees"), request("/shifts")]);
    state.employees = results[0];
    state.shifts = results[1];
}

function renderEmployees() {
    const rows = state.employees.map((employee) => `
        <tr>
            <td>${employee.id}</td><td>${employee.name}</td><td>${employee.email}</td><td>${employee.role}</td>
            <td>
                <button class="action-button" onclick="editEmployee(${employee.id})">Edit</button>
                <button class="action-button" onclick="deleteEmployee(${employee.id})">Delete</button>
            </td>
        </tr>`).join("");
    document.getElementById("employees-table").innerHTML = rows || emptyRow(5);
}

function renderShifts() {
    const rows = state.shifts.map((shift) => `
        <tr>
            <td>${shift.id}</td><td>${shift.shiftDate}</td><td>${shift.startTime}</td><td>${shift.endTime}</td><td>${shift.shiftType}</td>
            <td>
                <button class="action-button" onclick="editShift(${shift.id})">Edit</button>
                <button class="action-button" onclick="deleteShift(${shift.id})">Delete</button>
            </td>
        </tr>`).join("");
    document.getElementById("shifts-table").innerHTML = rows || emptyRow(6);
}

function renderRoster(rosters) {
    const rows = rosters.map((roster) => `
        <tr>
            <td>${roster.employee?.name || ""}</td>
            <td>${roster.shift?.shiftDate || ""}</td>
            <td>${roster.shift?.startTime || ""}</td>
            <td>${roster.shift?.endTime || ""}</td>
            <td>${roster.shift?.shiftType || ""}</td>
        </tr>`).join("");
    document.getElementById("roster-table").innerHTML = rows || emptyRow(5);
}

function renderSwapRequests(requests) {
    const rows = requests.map((item) => `
        <tr>
            <td>${item.id}</td>
            <td>${item.requester?.name || ""}</td>
            <td>${item.colleague?.name || ""}</td>
            <td>${item.shift?.shiftDate || ""} (${item.shift?.shiftType || ""})</td>
            <td>${item.colleagueStatus}</td>
            <td>${item.managerStatus}</td>
        </tr>`).join("");
    document.getElementById("swap-table").innerHTML = rows || emptyRow(6);
}

async function saveEmployee(event) {
    event.preventDefault();
    const id = document.getElementById("employee-id").value;
    const employee = {
        name: document.getElementById("employee-name").value,
        email: document.getElementById("employee-email").value,
        role: document.getElementById("employee-role").value
    };
    await saveData(id ? `/employees/${id}` : "/employees", id ? "PUT" : "POST", employee, "employee-form");
}

async function saveShift(event) {
    event.preventDefault();
    const id = document.getElementById("shift-id").value;
    const shift = {
        shiftDate: document.getElementById("shift-date").value,
        startTime: document.getElementById("shift-start").value + ":00",
        endTime: document.getElementById("shift-end").value + ":00",
        shiftType: document.getElementById("shift-type").value
    };
    await saveData(id ? `/shifts/${id}` : "/shifts", id ? "PUT" : "POST", shift, "shift-form");
}

async function saveRoster(event) {
    event.preventDefault();
    const roster = {
        employee: { id: Number(document.getElementById("roster-employee").value) },
        shift: { id: Number(document.getElementById("roster-shift").value) },
        weekStartDate: document.getElementById("roster-week").value
    };
    try {
        await request("/rosters", { method: "POST", body: JSON.stringify(roster) });
        document.getElementById("roster-form").classList.add("hidden");
        await loadPageData("roster");
    } catch (error) {
        if (error.status === 400) {
            showMessage("Employee already has an overlapping shift on this date.");
        } else {
            showMessage("Could not save the roster.");
        }
    }
}

async function saveSwapRequest(event) {
    event.preventDefault();
    const item = {
        requester: { id: Number(document.getElementById("swap-requester").value) },
        colleague: { id: Number(document.getElementById("swap-colleague").value) },
        shift: { id: Number(document.getElementById("swap-shift").value) },
        colleagueStatus: "PENDING",
        managerStatus: "PENDING"
    };
    await saveData("/swap-requests", "POST", item, "swap-form");
}

async function saveData(path, method, data, formId) {
    try {
        await request(path, { method, body: JSON.stringify(data) });
        document.getElementById(formId).classList.add("hidden");
        await loadPageData(document.querySelector(".page:not(.hidden)").id);
    } catch (error) {
        showMessage("Could not save the data.");
    }
}

function fillSelects() {
    const employeeOptions = state.employees.map((employee) => `<option value="${employee.id}">${employee.name}</option>`).join("");
    const shiftOptions = state.shifts.map((shift) => `<option value="${shift.id}">${shift.shiftDate} - ${shift.shiftType}</option>`).join("");
    ["roster-employee", "swap-requester", "swap-colleague"].forEach((id) => {
        document.getElementById(id).innerHTML = employeeOptions;
    });
    ["roster-shift", "swap-shift"].forEach((id) => {
        document.getElementById(id).innerHTML = shiftOptions;
    });
}

window.editEmployee = function (id) {
    const employee = state.employees.find((item) => item.id === id);
    document.getElementById("employee-id").value = employee.id;
    document.getElementById("employee-name").value = employee.name;
    document.getElementById("employee-email").value = employee.email;
    document.getElementById("employee-role").value = employee.role;
    document.getElementById("employee-form").classList.remove("hidden");
};

window.deleteEmployee = async function (id) {
    if (!confirm("Delete this employee?")) return;
    try {
        await request(`/employees/${id}`, { method: "DELETE" });
        await loadPageData("employees");
    } catch (error) {
        showMessage("Could not delete the employee.");
    }
};

window.editShift = function (id) {
    const shift = state.shifts.find((item) => item.id === id);
    document.getElementById("shift-id").value = shift.id;
    document.getElementById("shift-date").value = shift.shiftDate;
    document.getElementById("shift-start").value = shift.startTime.substring(0, 5);
    document.getElementById("shift-end").value = shift.endTime.substring(0, 5);
    document.getElementById("shift-type").value = shift.shiftType;
    document.getElementById("shift-form").classList.remove("hidden");
};

window.deleteShift = async function (id) {
    if (!confirm("Delete this shift?")) return;
    try {
        await request(`/shifts/${id}`, { method: "DELETE" });
        await loadPageData("shifts");
    } catch (error) {
        showMessage("Could not delete the shift.");
    }
};

function resetForm(formId) {
    document.getElementById(formId).reset();
    const hiddenId = document.querySelector(`#${formId} input[type="hidden"]`);
    if (hiddenId) hiddenId.value = "";
}

function emptyRow(columnCount) {
    return `<tr><td colspan="${columnCount}">No records found.</td></tr>`;
}

function showMessage(text) {
    document.getElementById("message").textContent = text;
}

function clearMessage() {
    document.getElementById("message").textContent = "";
}

showPage("employees");
