document.addEventListener("DOMContentLoaded", () => {
    fetch("/admin/api/statistics/monthly", {
        credentials: "include"
    })
        .then(res => res.json())
        .then(data => {
            const tbody = document.getElementById("statTable");
            tbody.innerHTML = "";

            Object.keys(data).forEach(month => {
                tbody.innerHTML += `
                    <tr>
                        <td>Tháng ${month}</td>
                        <td>${data[month]}</td>
                    </tr>
                `;
            });
        })
        .catch(err => console.error(err));
});
