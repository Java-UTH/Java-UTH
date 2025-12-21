package com.example.SP26SE025.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "Users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UserId")
    private Long id;

    @Column(name = "Username")
    private String username;

    @Column(name = "PasswordHash")
    private String password;

    @Column(name = "FullName")
    private String fullName;

    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "Role")
    private Role role;

    // SỬA DÒNG NÀY ĐỂ FIX LỖI SQL SERVER
    @Column(name = "Enabled", nullable = false, columnDefinition = "bit DEFAULT 1")
    private boolean enabled = true;

    // Constructors
    public User() {}

    // Getters và Setters
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

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    // Đã xóa 2 method lạ không cần thiết như bạn gợi ý
}