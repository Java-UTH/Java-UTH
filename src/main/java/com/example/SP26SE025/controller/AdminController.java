package com.example.SP26SE025.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.SP26SE025.security.CustomUserDetails;
import com.example.SP26SE025.security.JwtUtil;
import com.example.SP26SE025.service.CustomUserDetailsService;

@Controller
public class AdminController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @GetMapping("/loginAdmin")
    public String loginPage() {
        return "login-view-admin";
    }

    @GetMapping("/admin/home")
    public String home() {
        return "admin/home";
    }

    @PostMapping("/authenticateAdmin")
    public String authenticate(@RequestParam String username,
                               @RequestParam String password,
                               HttpServletResponse response) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            String token = jwtUtil.generateToken(username);
            Cookie cookie = new Cookie("jwt", token);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            response.addCookie(cookie);

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String role = userDetails.getAuthorities().iterator().next().getAuthority().replace("ROLE_", "").trim().toUpperCase();

            return switch (role) {
                case "ADMIN" -> "redirect:/admin/home";
                // case "CLINIC" -> "redirect:/clinic/home";
                // case "DOCTOR" -> "redirect:/doctor/dashboard";
                default -> "redirect:/loginAdmin?error=true";
            };
        } catch (Exception e) {
            e.printStackTrace(); // Thêm dòng này để in lỗi ra console
            return "redirect:/loginAdmin?error=true";
        }
    }
}
