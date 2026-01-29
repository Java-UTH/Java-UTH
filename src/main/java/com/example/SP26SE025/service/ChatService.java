package com.example.SP26SE025.service;

import com.example.SP26SE025.entity.ChatMessage;
import com.example.SP26SE025.entity.User;
import com.example.SP26SE025.repository.ChatMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ChatService {

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    public ChatMessage saveMessage(User sender, User recipient, String content) {
        ChatMessage message = ChatMessage.builder()
                .sender(sender)
                .recipient(recipient)
                .content(content)
                .timestamp(LocalDateTime.now())
                .seen(false)
                .build();
        return chatMessageRepository.save(message);
    }

    public List<ChatMessage> getChatHistory(User u1, User u2) {
        return chatMessageRepository.findChatHistory(u1, u2);
    }

    public void markAsSeen(User recipient, User sender) {
        List<ChatMessage> unseenMessages = chatMessageRepository.findByRecipientAndSeenFalse(recipient);
        for (ChatMessage m : unseenMessages) {
            if (m.getSender().getId().equals(sender.getId())) {
                m.setSeen(true);
            }
        }
        chatMessageRepository.saveAll(unseenMessages);
    }
}
