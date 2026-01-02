package com.example.SP26SE025.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "Users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UserId")
    private Long id;

    @Column(name = "Username", unique = true, nullable = false)
    private String username;

    @Column(name = "PasswordHash", nullable = false)
    private String password;

    @Column(name = "FullName")
    private String fullName;

    @Column(name = "Email")
    private String email;

    // Các trường dành riêng cho Bác sĩ (Role = DOCTOR)
    @Column(name = "Specialist")
    private String specialist; 

    @Column(name = "PhoneNumber")
    private String phoneNumber;

    // Enum Role
    @Enumerated(EnumType.STRING)
    @Column(name = "Role")
    private Role role;

    // --- SỬA Ở ĐÂY: Đổi boolean -> Boolean ---
    @Column(name = "Enabled", columnDefinition = "bit DEFAULT 1")
    private Boolean enabled = true; 

    public User() {}

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSpecialist() { return specialist; }
    public void setSpecialist(String specialist) { this.specialist = specialist; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    // --- SỬA Ở ĐÂY: Trả về true nếu enabled là true, trả về false nếu enabled là null hoặc false ---
    public boolean isEnabled() { 
        return Boolean.TRUE.equals(this.enabled); 
    }
    
    // --- SỬA Ở ĐÂY: Nhận tham số Boolean ---
    public void setEnabled(Boolean enabled) { 
        this.enabled = enabled; 
    }
}