package com.example.SP26SE025.controller;

import com.example.SP26SE025.dtos.NotificationDTO;
import com.example.SP26SE025.dtos.UserProfileDTO;
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

    // --- 1. DASHBOARD ---
    @GetMapping("/customer/home")
    public String customerHome() {
        // Trả về file src/main/resources/templates/customer/home.html
        return "customer/home"; 
    }

    // --- 2. [FR-8] QUẢN LÝ HỒ SƠ ---
    @GetMapping("/customer/profile")
    public String showProfile(Model model) {
        // Dữ liệu giả lập từ Database
        UserProfileDTO mockProfile = new UserProfileDTO(
            "Nguyễn Văn A", 
            "nguyenvana@gmail.com", 
            "0909123456", 
            LocalDate.of(1990, 5, 20), 
            "TYPE_2", 
            true, 
            "Đã phẫu thuật Lasik năm 2020"
        );
        model.addAttribute("userProfile", mockProfile);
        return "customer/profile";
    }

    @PostMapping("/customer/profile/update")
    public String updateProfile(@ModelAttribute UserProfileDTO userProfile) {
        // Logic lưu xuống DB sẽ viết ở đây
        System.out.println("Đang cập nhật: " + userProfile.getFullName());
        
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