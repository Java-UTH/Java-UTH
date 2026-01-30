document.addEventListener("DOMContentLoaded", () => {
    loadUsers();
    loadNotifications();
});

/* ===== MODAL ===== */
function openModal() {
    document.getElementById("notificationModal").style.display = "block";
}

function closeModal() {
    document.getElementById("notificationModal").style.display = "none";
}

/* ===== LOAD USERS ===== */
function loadUsers() {
    fetch("/admin/api/users", { credentials: "include" })
        .then(res => res.json())
        .then(users => {
            const select = document.getElementById("modalUserSelect");
            select.innerHTML = `
                <option value="">-- Chọn User --</option>
                <option value="ALL">🌍 Tất cả users</option>
            `;
            users.forEach(u => {
                select.innerHTML += `
                    <option value="${u.id}">
                        ${u.fullName ?? u.username} (ID: ${u.id})
                    </option>`;
            });
        });
}

/* ===== LOAD NOTIFICATIONS ===== */
function loadNotifications() {
    fetch("/admin/api/notifications", { credentials: "include" })
        .then(res => res.json())
        .then(list => {
            const tbody = document.getElementById("notificationTable");
            tbody.innerHTML = "";

            list.forEach(n => {
                tbody.innerHTML += `
                <tr>
                    <td>${n.id}</td>
                    <td>${n.user?.id ?? ""}</td>
                    <td>${n.user?.fullName ?? ""}</td>
                    <td><input value="${n.title ?? ""}"></td>
                    <td><textarea>${n.message ?? ""}</textarea></td>
                    <td>
                        <select>
                            <option value="SUCCESS" ${n.type==="SUCCESS"?"selected":""}>SUCCESS</option>
                            <option value="INFO" ${n.type==="INFO"?"selected":""}>INFO</option>
                            <option value="WARNING" ${n.type==="WARNING"?"selected":""}>WARNING</option>
                            <option value="CRITICAL" ${n.type==="CRITICAL"?"selected":""}>CRITICAL</option>
                        </select>
                    </td>
                    <td>${n.read ? "READ" : "UNREAD"}</td>
                    <td>
                        <button onclick="saveNotification(${n.id}, this)">Save</button>
                        <button onclick="markRead(${n.id})">Read</button>
                        <button class="danger" onclick="deleteNotification(${n.id})">Delete</button>
                    </td>
                </tr>`;
            });
        });
}

/* ===== SUBMIT ===== */
function submitNotification() {
    const userValue = document.getElementById("modalUserSelect").value;
    const title = document.getElementById("modalTitle").value;
    const message = document.getElementById("modalMessage").value;
    const type = document.getElementById("modalType").value;

    if (!userValue || !title || !message) {
        alert("Nhập đầy đủ thông tin");
        return;
    }

    if (userValue === "ALL") {
        if (!confirm("Gửi cho tất cả users?")) return;

        fetch("/admin/api/users", { credentials: "include" })
            .then(res => res.json())
            .then(users =>
                Promise.all(users.map(u =>
                    fetch("/admin/api/notifications", {
                        method: "POST",
                        credentials: "include",
                        headers: { "Content-Type": "application/json" },
                        body: JSON.stringify({
                            user: { id: u.id },
                            title, message, type
                        })
                    })
                ))
            )
            .then(() => {
                closeModal();
                loadNotifications();
            });
        return;
    }

    fetch("/admin/api/notifications", {
        method: "POST",
        credentials: "include",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
            user: { id: Number(userValue) },
            title, message, type
        })
    }).then(() => {
        closeModal();
        loadNotifications();
    });
}

/* ===== UPDATE ===== */
function saveNotification(id, btn) {
    const row = btn.closest("tr");
    const inputs = row.querySelectorAll("input, textarea, select");

    fetch(`/admin/api/notifications/${id}`, {
        method: "PUT",
        credentials: "include",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
            title: inputs[0].value,
            message: inputs[1].value,
            type: inputs[2].value
        })
    }).then(loadNotifications);
}

function markRead(id) {
    fetch(`/admin/api/notifications/${id}/read`, {
        method: "PUT",
        credentials: "include"
    }).then(loadNotifications);
}

function deleteNotification(id) {
    if (!confirm("Xóa notification?")) return;
    fetch(`/admin/api/notifications/${id}`, {
        method: "DELETE",
        credentials: "include"
    }).then(loadNotifications);
}
