import { useState, useEffect } from "react";
import "./App.css";

function App() {
  const [message, setMessage] = useState("Đang kết nối...");

  useEffect(() => {
    fetch("http://localhost:8080/hello")
      .then((res) => res.text())
      .then((data) => setMessage(data))
      .catch((_) => setMessage("Lỗi: Chưa bật Backend hoặc Database!"));
  }, []);

  return (
    <div style={{ padding: "50px", textAlign: "center" }}>
      <h1>Hệ thống AURA</h1>
      <h2 style={{ color: "green" }}>{message}</h2>
    </div>
  );
}

export default App;
