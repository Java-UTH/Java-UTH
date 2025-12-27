package com.example.SP26SE025.entity;

import jakarta.persistence.*;
import lombok.Data; // Nếu bạn dùng Lombok, nếu không hãy tự generate Getter/Setter
import java.time.LocalDateTime;

@Entity
@Table(name = "analysis_records")
@Data
public class AnalysisRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imageName; // Tên file
    private String imageUrl;  // Đường dẫn để hiển thị trên web
    
    @Column(name = "ai_result", columnDefinition = "NVARCHAR(MAX)") // Ép kiểu NVARCHAR để hỗ trợ tiếng Việt
    private String aiResult;

    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user; // Liên kết với bảng User

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}