package com.example.SP26SE025.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.SP26SE025.entity.User;
import com.example.SP26SE025.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // ======================================================
    // CÁC HÀM CŨ CỦA BẠN (GIỮ NGUYÊN)
    // ======================================================
    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public User save(User user) {
        // ✅ Nếu mật khẩu chưa được mã hóa thì mã hóa trước khi lưu
        if (user.getPassword() != null && !user.getPassword().startsWith("$2a$")) {
            String encodedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(encodedPassword);
            System.out.println(">>> Mật khẩu đã mã hóa: " + encodedPassword);
        }
        return userRepository.save(user);
    }

    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    // ======================================================
    // CẬP NHẬT MỚI: PHỤC VỤ NHIỆM VỤ THÀNH VIÊN 2
    // ======================================================

    /**
     * Hàm này rất quan trọng để ReportController lấy được thông tin User 
     * từ hệ thống Security (thông qua Principal).
     */
    public User findByUsername(String username) {
        // Trong hệ thống Spring Security thông thường, username có thể là Email.
        // Ta sẽ tận dụng hàm findByEmail đã có của bạn.
        Optional<User> user = userRepository.findByEmail(username);
        
        if (user.isPresent()) {
            return user.get();
        }
        
        // Nếu hệ thống của bạn dùng một cột 'username' riêng biệt trong DB, 
        // bạn cần tạo thêm hàm findByUsername trong UserRepository trước, 
        // sau đó mở comment dòng dưới đây:
        // return userRepository.findByUsername(username).orElse(null);
        
        return null;
    }
}