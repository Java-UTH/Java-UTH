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
        createUserIfNotExists("admin@example.com", "123", Role.ADMIN);
        createUserIfNotExists("clinic@example.com", "123", Role.CLINIC); 
        createUserIfNotExists("doctor@example.com", "123", Role.DOCTOR);
        createUserIfNotExists("customer@example.com", "123", Role.CUSTOMER);
    }

    private void createUserIfNotExists(String email, String rawPassword, Role role) {
        if (userRepository.findByEmail(email).isEmpty()) {
            User user = new User();
            String username = email.substring(0, email.indexOf("@")); 
            user.setUsername(username);
            user.setEmail(email);
            user.setPassword(passwordEncoder.encode(rawPassword)); 
            user.setRole(role);
            userRepository.save(user);
            System.out.println("Created user: " + email + " (" + username + ") with role " + role.name());
        }
    }
}