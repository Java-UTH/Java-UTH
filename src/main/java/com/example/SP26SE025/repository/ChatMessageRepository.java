package com.example.SP26SE025.repository;

import com.example.SP26SE025.entity.ChatMessage;
import com.example.SP26SE025.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    @Query("SELECT m FROM ChatMessage m WHERE " +
            "(m.sender = :u1 AND m.recipient = :u2) OR " +
            "(m.sender = :u2 AND m.recipient = :u1) " +
            "ORDER BY m.timestamp ASC")
    List<ChatMessage> findChatHistory(@Param("u1") User u1, @Param("u2") User u2);

    List<ChatMessage> findByRecipientAndSeenFalse(User recipient);
}
