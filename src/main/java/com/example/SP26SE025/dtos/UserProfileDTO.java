package com.example.SP26SE025.dtos;

import java.time.LocalDate;

public class UserProfileDTO {

    // 1. Khai báo các thuộc tính (Fields)
    private String fullName;
    private String email;
    private String phone;
    private LocalDate dob;           // Ngày sinh
    private String diabetesType;     // Loại tiểu đường: NONE, TYPE_1, TYPE_2, GESTATIONAL
    private boolean hypertension;    // Cao huyết áp: true (Có), false (Không)
    private String medicalHistory;   // Tiền sử bệnh án

    // 2. Constructor mặc định (No-args constructor)
    // Bắt buộc phải có để Spring Boot/Thymeleaf khởi tạo object rỗng khi binding form
    public UserProfileDTO() {
    }

    // 3. Constructor đầy đủ tham số (All-args constructor)
    // Dùng để tạo nhanh object khi giả lập dữ liệu (Mock data) trong Controller
    public UserProfileDTO(String fullName, String email, String phone, LocalDate dob, String diabetesType, boolean hypertension, String medicalHistory) {
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.dob = dob;
        this.diabetesType = diabetesType;
        this.hypertension = hypertension;
        this.medicalHistory = medicalHistory;
    }

    // 4. Các phương thức Getter và Setter (Viết thủ công)
    
    // --- Full Name ---
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    // --- Email ---
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // --- Phone ---
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    // --- Date of Birth (dob) ---
    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    // --- Diabetes Type ---
    public String getDiabetesType() {
        return diabetesType;
    }

    public void setDiabetesType(String diabetesType) {
        this.diabetesType = diabetesType;
    }

    // --- Hypertension (Boolean) ---
    // Lưu ý: Với kiểu boolean nguyên thủy, getter thường đặt tên là "is..." thay vì "get..."
    public boolean isHypertension() {
        return hypertension;
    }

    public void setHypertension(boolean hypertension) {
        this.hypertension = hypertension;
    }

    // --- Medical History ---
    public String getMedicalHistory() {
        return medicalHistory;
    }

    public void setMedicalHistory(String medicalHistory) {
        this.medicalHistory = medicalHistory;
    }

    // 5. Phương thức toString() (Tùy chọn - Rất hữu ích khi debug)
    // Giúp in ra nội dung object thay vì địa chỉ bộ nhớ khi dùng System.out.println()
    @Override
    public String toString() {
        return "UserProfileDTO{" +
                "fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", dob=" + dob +
                ", diabetesType='" + diabetesType + '\'' +
                ", hypertension=" + hypertension +
                ", medicalHistory='" + medicalHistory + '\'' +
                '}';
    }
}