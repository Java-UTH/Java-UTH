const users = [
  {id:1,email:"user@gmail.com",role:"USER",status:"ACTIVE"}
];

const tbody = document.querySelector("tbody");
users.forEach(u=>{
  tbody.innerHTML += `
   <tr>
     <td>${u.id}</td>
     <td>${u.email}</td>
     <td>${u.role}</td>
     <td>${u.status}</td>
     <td><button>Toggle</button></td>
   </tr>`;
});
