package com.example.SP26SE025.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "alerts")
public class Alert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ID người dùng nhận cảnh báo
    @Column(nullable = false)
    private Long userId;

    // Mức độ rủi ro: LOW / MEDIUM / HIGH
    @Column(nullable = false)
    private String level;

    // Nội dung cảnh báo / khuyến nghị
    @Column(length = 500)
    private String message;

    // Thời điểm tạo cảnh báo
    private LocalDateTime createdAt = LocalDateTime.now();

    // ===== Constructors =====
    public Alert() {
    }

    public Alert(Long userId, String level, String message) {
        this.userId = userId;
        this.level = level;
        this.message = message;
    }

    // ===== Getter & Setter =====
    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
