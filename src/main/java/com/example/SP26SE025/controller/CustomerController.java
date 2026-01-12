package com.example.SP26SE025.controller;

import com.example.SP26SE025.dtos.NotificationDTO;
import com.example.SP26SE025.dtos.UserProfileDTO;
import com.example.SP26SE025.entity.Notification;
import com.example.SP26SE025.entity.User;
import com.example.SP26SE025.service.NotificationService;
import com.example.SP26SE025.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
public class CustomerController {

    @Autowired
    private UserService userService;

    @Autowired
    private NotificationService notificationService;
    
    @Autowired
    private com.example.SP26SE025.repository.UserRepository userRepository;

    // --- 1. DASHBOARD ---
    @GetMapping("/customer/home")
    public String customerHome(Model model, Principal principal) {
        if (principal != null) {
            User user = userRepository.findByEmail(principal.getName()).orElse(null);
            if (user != null) {
                model.addAttribute("currentUser", user);
            }
        }
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
        userProfile.setAvatarPath(user.getAvatarPath());
        userProfile.setDob(user.getDob());
        userProfile.setDiabetesType(user.getDiabetesType());
        userProfile.setHypertension(user.getHypertension());
        userProfile.setMedicalHistory(user.getMedicalHistory());

        model.addAttribute("userProfile", userProfile);
        return "customer/profile";
    }

    @PostMapping("/customer/profile/update")
    public String updateProfile(@Valid @ModelAttribute UserProfileDTO userProfile, BindingResult result, Model model,
                               @RequestParam(value = "avatarFile", required = false) MultipartFile avatarFile) {
        // Kiểm tra lỗi validation
        if (result.hasErrors()) {
            System.out.println(">>> Validation errors: " + result.getErrorCount());
            model.addAttribute("userProfile", userProfile);
            return "customer/profile";
        }

        // Lấy user hiện tại từ Security Context
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        
        User user = userService.findByEmail(email);
        
        if (user != null) {
            // Xử lý upload avatar nếu có
            if (avatarFile != null && !avatarFile.isEmpty()) {
                try {
                    String avatarPath = uploadAvatar(avatarFile, user.getId());
                    user.setAvatarPath(avatarPath);
                    System.out.println(">>> Avatar uploaded: " + avatarPath);
                } catch (Exception e) {
                    System.out.println(">>> LỖI upload avatar: " + e.getMessage());
                }
            }

            // Cập nhật thông tin từ DTO sang Entity
            user.setFullName(userProfile.getFullName());
            user.setPhoneNumber(userProfile.getPhone());
            user.setDob(userProfile.getDob());
            user.setDiabetesType(userProfile.getDiabetesType());
            user.setHypertension(userProfile.getHypertension());
            user.setMedicalHistory(userProfile.getMedicalHistory());
            
            // Lưu vào database - gọi updateProfile với userId
            userService.updateProfile(user.getId(), user);
            
            System.out.println(">>> Đã cập nhật profile cho user: " + email);
        } else {
            System.out.println(">>> LỖI: Không tìm thấy user để cập nhật: " + email);
        }
        
        // Quay lại trang profile kèm thông báo thành công
        return "redirect:/customer/profile?success";
    }

    /**
     * Upload avatar file
     */
    private String uploadAvatar(MultipartFile file, Long userId) throws Exception {
        // Lưu file vào thư mục uploads trong target/classes (nơi Spring Boot chạy)
        String uploadDir = System.getProperty("user.dir") + "/target/classes/static/images/uploads";
        File uploadFolder = new File(uploadDir);
        
        System.out.println(">>> Uploading to: " + uploadDir);
        
        if (!uploadFolder.exists()) {
            boolean created = uploadFolder.mkdirs();
            System.out.println(">>> Upload folder created: " + created);
        }

        // Kiểm tra loại file
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new Exception("File phải là ảnh");
        }

        // Kiểm tra kích thước file
        if (file.getSize() > 10 * 1024 * 1024) { // 10MB
            throw new Exception("File quá lớn, tối đa 10MB");
        }

        try {
            // Tạo tên file unique
            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null || originalFilename.isEmpty()) {
                throw new Exception("Tên file không hợp lệ");
            }
            
            String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String newFilename = userId + "_" + UUID.randomUUID().toString() + fileExtension;
            
            // Lưu file
            File destFile = new File(uploadDir, newFilename);
            file.transferTo(destFile);
            
            System.out.println(">>> Avatar saved: " + newFilename);
            
            // Trả về đường dẫn tương đối để hiển thị
            return "/images/uploads/" + newFilename;
        } catch (Exception e) {
            System.out.println(">>> ERROR uploading file: " + e.getMessage());
            e.printStackTrace();
            throw new Exception("Lỗi upload ảnh: " + e.getMessage());
        }
    }

    // --- 3. [FR-9] THÔNG BÁO ---
    @GetMapping("/customer/notifications")
    public String showNotifications(Model model) {
        // Lấy user hiện tại từ Security Context
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        
        User user = userService.findByEmail(email);
        
        if (user != null) {
            // Lấy tất cả notifications từ database
            List<Notification> notifs = notificationService.getAllNotifications(user);
            
            // Chuyển đổi Entity sang DTO để hiển thị
            List<NotificationDTO> notifDTOs = notifs.stream()
                .map(notif -> new NotificationDTO(
                    notif.getTitle(),
                    notif.getMessage(),
                    notif.getType(),
                    notif.getCreatedAt(),
                    notif.isRead()
                ))
                .collect(Collectors.toList());
            
            model.addAttribute("notifications", notifDTOs);
        } else {
            model.addAttribute("notifications", new ArrayList<>());
        }
        
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