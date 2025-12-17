## 1️⃣ Cấu hình cơ sở dữ liệu (SQL Server)

Mở và chỉnh sửa file:

```
src/main/resources/application.properties
```

Thêm hoặc cập nhật cấu hình sau:

```properties
# ===============================
# Database Configuration
# ===============================
spring.datasource.url=jdbc:mysql://localhost:3306/javauth?useSSL=false&serverTimezone=UTCdatabaseName=YourDatabaseName;encrypt=true;trustServerCertificate=true;
spring.datasource.username=root
spring.datasource.password=locdeptrai12
spring.datasource.driver-class-name=com.microsoft.sqlserver.jdbc.SQLServerDriver

# ===============================
# JPA / Hibernate Configuration
# ===============================
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

📌 **Lưu ý quan trọng**:

* Thay `YourDatabaseName` bằng tên database SQL Server của bạn
* Thay `YourUsername` và `YourPassword` bằng tài khoản SQL Server tương ứng
* Đảm bảo SQL Server đang chạy và cho phép kết nối qua cổng **1433**

---

## 2️⃣ Build dự án

Sử dụng Maven để build project:

```bash
mvn clean install
```

---

## 3️⃣ Chạy ứng dụng Spring Boot

Sau khi build thành công, chạy ứng dụng bằng lệnh:

```bash
mvn spring-boot:run
```

---

## 4️⃣ Truy cập ứng dụng

Mở trình duyệt và truy cập các đường dẫn sau:

### 🏠 Trang chủ

```
http://localhost:8080/
```

### 👤 Trang đăng nhập Khách hàng / Người dùng công cộng

```
http://localhost:8080/login
```

### 🔐 Trang đăng nhập Admin / Manager / Staff

```
http://localhost:8080/loginAdmin
```

---
