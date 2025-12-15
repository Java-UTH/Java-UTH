package com.example.SP26SE025.config;

import com.example.SP26SE025.entity.Role;
import com.example.SP26SE025.entity.User;
import com.example.SP26SE025.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        createUserIfNotExists("admin@example.com", "123", Role.ADMIN);
        //createUserIfNotExists("manager@example.com", "123", Role.MANAGER);
        createUserIfNotExists("doctor@example.com", "123", Role.DOCTOR);
        createUserIfNotExists("consultant@example.com", "123", Role.CONSULTANT);
        createUserIfNotExists("customer@example.com", "123", Role.CUSTOMER);
    }

    private void createUserIfNotExists(String email, String rawPassword, Role role) {
        if (userRepository.findByEmail(email).isEmpty()) {
            User user = new User();
            user.setEmail(email);
            user.setPassword(passwordEncoder.encode(rawPassword));
            user.setRole(role);
            userRepository.save(user);
            System.out.println("Created user: " + email + " | role=" + role);
        }
    }
}
