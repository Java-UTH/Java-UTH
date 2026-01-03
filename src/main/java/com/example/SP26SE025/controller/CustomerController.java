package com.example.SP26SE025.controller;

import com.example.SP26SE025.dtos.NotificationDTO;
import com.example.SP26SE025.dtos.UserProfileDTO;
import com.example.SP26SE025.entity.User;
import com.example.SP26SE025.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
public class CustomerController {

    @Autowired
    private UserService userService;

    // --- 1. DASHBOARD ---
    @GetMapping("/customer/home")
    public String customerHome() {
        // Trả về file src/main/resources/templates/customer/home.html
        return "customer/home"; 
    }

    // --- 2. [FR-8] QUẢN LÝ HỒ SƠ ---
    @GetMapping("/customer/profile")
    public String showProfile(Model model) {
        // Lấy user hiện tại từ Security Context
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        
        User user = userService.findByEmail(email);
        
        if (user == null) {
            // Nếu không tìm thấy user, sử dụng dữ liệu mặc định
            user = new User();
            user.setFullName("Người dùng mới");
            user.setEmail(email);
        }

        // Chuyển đổi từ User Entity sang UserProfileDTO
        UserProfileDTO userProfile = new UserProfileDTO();
        userProfile.setFullName(user.getFullName());
        userProfile.setEmail(user.getEmail());
        userProfile.setPhone(user.getPhoneNumber());
        userProfile.setDob(user.getDob());
        userProfile.setDiabetesType(user.getDiabetesType());
        userProfile.setHypertension(user.getHypertension());
        userProfile.setMedicalHistory(user.getMedicalHistory());

        model.addAttribute("userProfile", userProfile);
        return "customer/profile";
    }

    @PostMapping("/customer/profile/update")
    public String updateProfile(@ModelAttribute UserProfileDTO userProfile) {
        // Lấy user hiện tại từ Security Context
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        
        User user = userService.findByEmail(email);
        
        if (user != null) {
            // Cập nhật thông tin từ DTO sang Entity
            user.setFullName(userProfile.getFullName());
            user.setPhoneNumber(userProfile.getPhone());
            user.setDob(userProfile.getDob());
            user.setDiabetesType(userProfile.getDiabetesType());
            user.setHypertension(userProfile.getHypertension());
            user.setMedicalHistory(userProfile.getMedicalHistory());
            
            // Lưu vào database
            userService.updateProfile(user);
            
            System.out.println(">>> Đã cập nhật profile cho user: " + email);
        } else {
            System.out.println(">>> LỖI: Không tìm thấy user để cập nhật: " + email);
        }
        
        // Quay lại trang profile kèm thông báo thành công
        return "redirect:/customer/profile?success";
    }

    // --- 3. [FR-9] THÔNG BÁO ---
    @GetMapping("/customer/notifications")
    public String showNotifications(Model model) {
        // Dữ liệu giả lập
        List<NotificationDTO> notifs = Arrays.asList(
            new NotificationDTO("Kết quả AI hoàn tất", "Ảnh OCT #IMG-001 của bạn bình thường.", "SUCCESS", LocalDateTime.now().minusMinutes(5), false),
            new NotificationDTO("Cảnh báo sức khỏe", "Phát hiện dấu hiệu tổn thương võng mạc.", "WARNING", LocalDateTime.now().minusHours(2), true),
            new NotificationDTO("Bảo mật", "Phát hiện đăng nhập lạ.", "INFO", LocalDateTime.now().minusDays(1), true)
        );
        model.addAttribute("notifications", notifs);
        return "customer/notifications";
    }
    
    // --- Các trang Placeholder khác ---
    @GetMapping("/customer/reports/analysis")
    public String showAnalysis(Model model) { 
        model.addAttribute("historyList", new ArrayList<>());
        return "customer/analysis_report"; 
    }
    
    // Điều hướng tạm các link chưa làm để không lỗi 404
    @GetMapping({"/customer/upload", "/customer/doctor-chat", "/customer/packages", "/test-services/customer"})
    public String temporaryRedirect() {
        return "redirect:/customer/home";
    }
}