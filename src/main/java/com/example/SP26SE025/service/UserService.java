package com.example.SP26SE025.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.SP26SE025.entity.User;
import com.example.SP26SE025.repository.UserRepository;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;  // ✅ Thêm mã hóa mật khẩu

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

            // ✅ In ra mật khẩu đã mã hóa (chỉ dùng để kiểm tra, có thể xoá dòng này)
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
}
