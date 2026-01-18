# AURA - System for Retinal Vascular Health Screening
### (Hệ Thống Sức Khỏe Võng Mạc)

![Project Status](https://img.shields.io/badge/Status-In%20Development-orange?style=for-the-badge)
![License](https://img.shields.io/badge/License-MIT-blue?style=for-the-badge)
![Version](https://img.shields.io/badge/Version-1.0.0-green?style=for-the-badge)

<a id="demo"></a>
## 🌐 Demo & Deployment
[![SP26SE025](https://img.shields.io/badge/SP26SE025-Render-success?style=for-the-badge)](https://sp26se025.onrender.com)

> **Mã dự án:** SP26SE025  
> **Chủ đề:** Y tế dự phòng & Trí tuệ nhân tạo (AI)

<p align="center">
  <a href="#tong-quan">Tổng Quan</a> •
  <a href="#tinh-nang">Tính Năng</a> •
  <a href="#cong-nghe">Công Nghệ</a> •
  <a href="#cau-truc">Cài Đặt</a> •
  <a href="#doi-ngu">Đội Ngũ</a>
</p>

---

<a id="tong-quan"></a>
## 🏥 Tổng Quan (Overview)

**AURA** (Comprehensive Understanding Retinal Analysis) là hệ thống hỗ trợ quyết định lâm sàng (CDS) sử dụng Trí tuệ nhân tạo để phân tích hình ảnh võng mạc. Hệ thống giúp phát hiện sớm các nguy cơ về tim mạch, tiểu đường và đột quỵ thông qua các bất thường của mạch máu nhỏ trong mắt.

### 🌟 Tại sao AURA ra đời?
1.  **Bối cảnh:** Các bệnh lý tim mạch và tiểu đường thường tiến triển âm thầm. Việc sàng lọc diện rộng gặp khó khăn do thiếu nhân lực và chi phí cao.
2.  **Giải pháp:** AURA biến đôi mắt thành "cửa sổ" sức khỏe, cung cấp công cụ sàng lọc nhanh chóng, không xâm lấn, giúp bác sĩ đưa ra quyết định chính xác hơn và mang y tế dự phòng đến gần hơn với cộng đồng.

---

<a id="tinh-nang"></a>
## 🚀 Tính Năng Chính (Key Features)

Hệ thống được chia thành 4 phân hệ chính phục vụ các đối tượng khác nhau:

### 👤 Dành cho Người dùng (Bệnh nhân)
* **Sàng lọc AI:** Tải lên ảnh chụp võng mạc (đáy mắt/OCT) và nhận kết quả phân tích rủi ro trong vài giây.
* **Hồ sơ sức khỏe:** Quản lý lịch sử khám, xem hình ảnh trực quan hóa các vùng tổn thương.
* **Tư vấn trực tuyến:** Chat trực tiếp với bác sĩ để nhận lời khuyên dựa trên kết quả AI.
* **Thanh toán:** Mua gói dịch vụ và quản lý giao dịch.

### 👨‍⚕️ Dành cho Bác sĩ
* **Hỗ trợ chẩn đoán:** Xem kết quả AI gợi ý, xác nhận hoặc chỉnh sửa chẩn đoán.
* **Quản lý bệnh nhân:** Theo dõi hồ sơ bệnh án, ghi chú y tế.
* **Tương tác:** Gửi khuyến nghị và tư vấn cho bệnh nhân.

### 🏥 Dành cho Phòng khám
* **Quản lý tổ chức:** Quản lý đội ngũ bác sĩ và danh sách bệnh nhân.
* **Xử lý hàng loạt:** Upload và phân tích số lượng lớn ảnh cho các chiến dịch khám sức khỏe.
* **Báo cáo:** Thống kê hiệu suất và báo cáo tổng hợp.

### 🛡️ Dành cho Quản trị viên (Admin)
* **Dashboard:** Theo dõi sức khỏe hệ thống, doanh thu và hiệu suất mô hình AI.
* **Phân quyền:** Quản lý người dùng, vai trò (RBAC) và kiểm soát truy cập.
* **Cấu hình AI:** Quản lý tham số và huấn luyện lại mô hình.

---

<a id="cong-nghe"></a>
## 🛠 Công Nghệ Sử Dụng (Tech Stack)

| Phân hệ | Công nghệ |
| :--- | :--- |
| **Backend** | ![Java](https://img.shields.io/badge/Java-Spring%20Boot-red) |
| **AI Core** | ![Python](https://img.shields.io/badge/Python-PyTorch-yellow) |
| **Frontend** | ![Thymeleaf](https://img.shields.io/badge/Thymeleaf-Server%20Side-green) ![HTML](https://img.shields.io/badge/HTML-5-orange) ![CSS](https://img.shields.io/badge/CSS-3-blue) ![JavaScript](https://img.shields.io/badge/JavaScript-ES6-yellow) |
| **Database** | ![PostgreSQL](https://img.shields.io/badge/PostgreSQL-Relational-336791) ![MongoDB](https://img.shields.io/badge/MongoDB-NoSQL-47A248) |
| **Cloud/Infra** | ![Render](https://img.shields.io/badge/Render-Deployment-46E3B7) |

---

<a id="doi-ngu"></a>
| Tên nhánh (Branch) | Thành viên | Module / Chức năng chính | Chi tiết yêu cầu (Functional Requirements) |
| :--- | :--- | :--- | :--- |
| **`duoc`** | **Nguyễn Quang Được** | **Người dùng: Auth & Profile** | **[FR-1, FR-8, FR-9]**<br>• Đăng ký/Đăng nhập & API Auth<br>• Quản lý hồ sơ cá nhân<br>• Màn hình thông báo |
| **`thanh`** | **Phạm Chí Thành** | **Người dùng: Phân tích AI** | **[FR-2, 3, 4, 6, 7]**<br>• Upload ảnh & Gọi API AI<br>• Hiển thị kết quả & Trực quan hóa<br>• Lịch sử phân tích & Xuất báo cáo |
| **`nam`** | **Nguyễn Hoài Nam** | **Bác sĩ (Doctor Portal)** | **[FR-13 ➝ FR-21]**<br>• Xác nhận kết quả AI<br>• Ghi chú y tế & Tư vấn bệnh nhân<br>• Quản lý hồ sơ bệnh nhân được chỉ định |
| **`tuan`** | **Đoàn Anh Tuấn** | **Phòng khám (Clinic Portal)** | **[FR-22 ➝ FR-30]**<br>• Quản lý tổ chức & Bác sĩ<br>• Upload hàng loạt (Batch Processing)<br>• Báo cáo tổng hợp & Thanh toán tổ chức |
| **`loc`** | **Nguyễn Thành Lộc** | **Quản trị (Admin Portal)** | **[FR-31 ➝ FR-39]**<br>• Dashboard thống kê toàn hệ thống<br>• Phân quyền người dùng (RBAC)<br>• Cấu hình tham số hệ thống |
| **`loc_ai`** | **Nguyễn Thành Lộc** | **AI Service Core** | **[Phần AI Core]**<br>• Xây dựng API AI (Python)<br>• Xử lý ảnh & Model training<br>• Tích hợp kết quả trả về cho Backend |

---

<a id="cau-truc"></a>
## 📂 Cấu Trúc Dự Án (Project Structure)

```bash
SP26SE025/
├── ai-service/
├── src/
│   └── main/
│       ├── java/com/example/SP26SE025/
│       │   ├── config/
│       │   ├── controller/
│       │   ├── dtos/
│       │   ├── entity/
│       │   ├── repository/
│       │   ├── security/
│       │   ├── service/
│       │   └── SP26SE025Application.java
│       └── resources/
├── mvnw
├── mvnw.cmd
├── pom.xml
└── README.md
