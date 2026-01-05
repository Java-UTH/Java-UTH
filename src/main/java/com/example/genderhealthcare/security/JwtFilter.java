package com.example.genderhealthcare.security;

import com.example.genderhealthcare.service.CustomUserDetailsService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String token = null;
        Cookie[] cookies = request.getCookies();

        System.out.println("🔹 Request URI: " + request.getRequestURI());

        // 1. Lấy Token từ Cookie
        if (cookies != null) {
            for (Cookie c : cookies) {
                if (c.getName().equals("jwt")) {
                    token = c.getValue();
                    break;
                }
            }
        }

        System.out.println("🔹 JWT Token from cookie: " + token);

        String username = null;
        boolean tokenValid = false;

        // 2. Xác thực Token
        if (token != null) {
            try {
                if (jwtUtil.validateToken(token)) {
                    username = jwtUtil.getUsernameFromToken(token);
                    tokenValid = true;
                    System.out.println("✅ Token valid for user: " + username);
                } else {
                    System.out.println("❌ Token invalid");
                }
            } catch (Exception e) {
                System.out.println("❌ Token validation exception: " + e.getMessage());
            }
        }

        // 3. Thiết lập ngữ cảnh bảo mật
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null && tokenValid) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());
            auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(auth);

            System.out.println("🔐 Authenticated user: " + username);
            System.out.println("🔐 Roles: " + userDetails.getAuthorities());
        } else if (username != null) {
            System.out.println("⚠️ Security context already set or token invalid");
        }

        filterChain.doFilter(request, response);
    }
}
