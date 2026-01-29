document.addEventListener("DOMContentLoaded", loadPackages);

// ========= LOAD =========
function loadPackages() {
    fetch("/admin/api/packages", { credentials: "include" })
        .then(res => res.json())
        .then(packages => {
            const tbody = document.getElementById("packageTableBody");
            tbody.innerHTML = "";

            packages.forEach(p => {
                tbody.innerHTML += `
                <tr>
                    <td>${p.id}</td>
                    <td><input value="${p.packageName || ''}"></td>
                    <td><input type="number" value="${p.price || 0}"></td>
                    <td><input value="${p.period || ''}"></td>
                    <td><input value="${p.features || ''}"></td>
                    <td style="text-align:center">
                        <input type="checkbox" ${p.active ? "checked" : ""} >
                    </td>
                    <td>
                        <button onclick="savePackage(${p.id}, this)">Save</button>
                        </button>
                        <button class="danger" onclick="deletePackage(${p.id})">
                            Delete
                        </button>
                    </td>
                </tr>`;
            });
        })
        .catch(() => alert("Không tải được package"));
}

// ========= ADD =========
function addPackage() {
    const data = {
        packageName: "New Package",
        price: 0,
        period: "/month",
        features: "",
        active: true
    };

    fetch("/admin/api/packages", {
        method: "POST",
        credentials: "include",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(data)
    })
    .then(() => loadPackages())
    .catch(() => alert("Lỗi thêm package"));
}

// ========= UPDATE =========
function savePackage(id, btn) {
    const row = btn.closest("tr");
    const inputs = row.querySelectorAll("input");

    const data = {
        packageName: inputs[0].value,
        price: parseFloat(inputs[1].value),
        period: inputs[2].value,
        features: inputs[3].value,
        active: inputs[4].checked
    };

    fetch(`/admin/api/packages/${id}`, {
        method: "PUT",
        credentials: "include",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(data)
    })
    .then(() => loadPackages())
    .catch(() => alert("Lỗi cập nhật package"));
}

// ========= TOGGLE =========
function togglePackage(id) {
    fetch(`/admin/api/packages/${id}/toggle`, {
        method: "PUT",
        credentials: "include"
    })
    .then(() => loadPackages())
    .catch(() => alert("Lỗi bật/tắt package"));
}

// ========= DELETE =========
function deletePackage(id) {
    if (!confirm("Bạn có chắc muốn xóa package này không?")) return;

    fetch(`/admin/api/packages/${id}`, {
        method: "DELETE",
        credentials: "include"
    })
    .then(() => loadPackages())
    .catch(() => alert("Lỗi xóa package"));
}
