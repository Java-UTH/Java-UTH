package com.example.genderhealthcare.entity;

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
    
    @Column(columnDefinition = "TEXT")
    private String aiResult;  // Kết quả AI trả về

    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user; // Liên kết với bảng User

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}