package com.example.SP26SE025.config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.SP26SE025.entity.Role;
import com.example.SP26SE025.entity.User;
import com.example.SP26SE025.repository.UserRepository;

@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {

        // NHỚ THAY ĐỔI CHỨC NĂNG CHO PHÙ HỢP VỚI WEB VÕNG MẠC
        // createUserIfNotExists("admin@example.com", "123", Role.ADMIN);// QUẢN TRỊ VIÊN(ADMIN)
        // createUserIfNotExists("manager@example.com", "123", Role.CLINIC); // PHÒNG KHÁM
        // createUserIfNotExists("staff@example.com", "123", Role.DOCTOR); // BÁC SĨ
        // createUserIfNotExists("customer@example.com", "123", Role.CUSTOMER);  // NGƯỜI DÙNG/ KHÁCH HÀNG

        // CỦA WEB CŨ 
        createUserIfNotExists("admin@example.com", "123", Role.ADMIN);
        createUserIfNotExists("clinic@example.com", "123", Role.CLINIC); //PHÒNG KHÁM
        createUserIfNotExists("staff@example.com", "123", Role.STAFF);
        createUserIfNotExists("consultant@example.com", "123", Role.CONSULTANT);
        createUserIfNotExists("customer@example.com", "123", Role.CUSTOMER);
    }

    private void createUserIfNotExists(String email, String rawPassword, Role role) {
        if (userRepository.findByEmail(email).isEmpty()) {
            User user = new User();
            user.setEmail(email);
            user.setPassword(passwordEncoder.encode(rawPassword)); // mã hóa mật khẩu
            user.setRole(role);
            userRepository.save(user);
            System.out.println("Created user: " + email + " with role " + role.name());
        }
    }
}
