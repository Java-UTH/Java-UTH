document.addEventListener("DOMContentLoaded", loadClinics);

function loadClinics() {
    fetch("/admin/api/clinics", { credentials: "include" })
        .then(res => res.json())
        .then(clinics => {
            const tbody = document.getElementById("clinicTable");
            tbody.innerHTML = "";

            clinics.forEach(c => {
                tbody.innerHTML += `
                <tr>
                    <td>${c.id}</td>

                    <td><input value="${c.clinicName || ""}"></td>
                    <td><input value="${c.address || ""}"></td>
                    <td><input value="${c.phone || ""}"></td>

                    <td>
                        <button onclick="saveClinic(${c.id}, this)">Save</button>

                        ${c.verificationStatus === "PENDING"
                            ? `<button onclick="approveClinic(${c.id})">Approve</button>`
                            : ""
                        }

                        ${c.verificationStatus === "FULFILLED"
                            ? `<button class="danger" onclick="suspendClinic(${c.id})">Suspend</button>`
                            : ""
                        }
                    </td>

                    <td>${c.verificationStatus}</td>
                </tr>`;
            });
        })
        .catch(() => alert("Không tải được danh sách clinic"));
}

// ================= UPDATE CLINIC =================
function saveClinic(id, btn) {
    const row = btn.closest("tr");
    const inputs = row.querySelectorAll("input");

    const data = {
        clinicName: inputs[0].value,
        address: inputs[1].value,
        phone: inputs[2].value
    };

    fetch(`/admin/api/clinics/${id}`, {
        method: "PUT",
        credentials: "include",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(data)
    })
    .then(res => {
        if (!res.ok) throw new Error();
        alert("Cập nhật clinic thành công");
        loadClinics();
    })
    .catch(() => alert("Lỗi cập nhật clinic"));
}

// ================= APPROVE =================
function approveClinic(id) {
    fetch(`/admin/api/clinics/${id}/approve`, {
        method: "PUT",
        credentials: "include"
    })
    .then(loadClinics)
    .catch(() => alert("Lỗi duyệt clinic"));
}

// ================= SUSPEND =================
function suspendClinic(id) {
    if (!confirm("Bạn có chắc muốn tạm ngưng clinic này?")) return;

    fetch(`/admin/api/clinics/${id}/suspend`, {
        method: "PUT",
        credentials: "include"
    })
    .then(loadClinics)
    .catch(() => alert("Lỗi tạm ngưng clinic"));
}
