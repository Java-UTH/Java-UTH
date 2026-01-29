package com.example.SP26SE025.controller;

import com.example.SP26SE025.entity.ChatMessage;
import com.example.SP26SE025.entity.Role;
import com.example.SP26SE025.entity.User;
import com.example.SP26SE025.repository.UserRepository;
import com.example.SP26SE025.service.ChatService;
import com.example.SP26SE025.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;
import java.util.List;

@Controller
public class ChatController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private ChatService chatService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/customer/chat")
    public String showChatPage(Model model, Principal principal) {
        User currentUser = userService.findByEmail(principal.getName());
        List<User> doctors = userRepository.findByRole(Role.DOCTOR);
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("doctors", doctors);
        return "customer/chat_list";
    }

    @GetMapping("/customer/chat/{doctorId}")
    public String showConversation(@PathVariable Long doctorId, Model model, Principal principal) {
        User currentUser = userService.findByEmail(principal.getName());
        User doctor = userRepository.findById(doctorId).orElseThrow();

        List<ChatMessage> history = chatService.getChatHistory(currentUser, doctor);
        chatService.markAsSeen(currentUser, doctor);

        model.addAttribute("currentUser", currentUser);
        model.addAttribute("doctor", doctor);
        model.addAttribute("history", history);
        return "customer/chat_room";
    }

    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload ChatRequest chatRequest) {
        User sender = userRepository.findById(chatRequest.getSenderId()).orElseThrow();
        User recipient = userRepository.findById(chatRequest.getRecipientId()).orElseThrow();

        ChatMessage savedMsg = chatService.saveMessage(sender, recipient, chatRequest.getContent());

        // Gửi tin nhắn đến người nhận qua WebSocket (Sử dụng Email làm định danh)
        messagingTemplate.convertAndSendToUser(
                recipient.getEmail(), "/queue/messages",
                savedMsg);
    }

    public static class ChatRequest {
        private Long senderId;
        private Long recipientId;
        private String content;

        public Long getSenderId() {
            return senderId;
        }

        public void setSenderId(Long senderId) {
            this.senderId = senderId;
        }

        public Long getRecipientId() {
            return recipientId;
        }

        public void setRecipientId(Long recipientId) {
            this.recipientId = recipientId;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
