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
                    <td>${c.name}</td>
                    <td>${c.address}</td>
                    <td>${c.phone}</td>
                    <td>${c.status}</td>
                    <td>
                        ${c.status === "PENDING" ? 
                            `<button onclick="approveClinic(${c.id})">Approve</button>` : ""
                        }
                        ${c.status === "APPROVED" ?
                            `<button onclick="suspendClinic(${c.id})">Suspend</button>` : ""
                        }
                    </td>
                </tr>`;
            });
        });
}

function approveClinic(id) {
    fetch(`/admin/api/clinics/${id}/approve`, {
        method: "PUT",
        credentials: "include"
    }).then(loadClinics);
}

function suspendClinic(id) {
    fetch(`/admin/api/clinics/${id}/suspend`, {
        method: "PUT",
        credentials: "include"
    }).then(loadClinics);
}
