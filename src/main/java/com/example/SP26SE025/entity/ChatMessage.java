package com.example.SP26SE025.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "ChatMessages")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "SenderId", nullable = false)
    private User sender;

    @ManyToOne
    @JoinColumn(name = "RecipientId", nullable = false)
    private User recipient;

    @Column(name = "Content", columnDefinition = "nvarchar(MAX)", nullable = false)
    private String content;

    @Column(name = "Timestamp")
    private LocalDateTime timestamp;

    @Column(name = "IsSeen")
    private boolean seen;
}
