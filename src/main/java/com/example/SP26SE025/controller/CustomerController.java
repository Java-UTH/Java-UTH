package com.example.SP26SE025.controller;

import com.example.SP26SE025.dtos.NotificationDTO;
import com.example.SP26SE025.dtos.UserProfileDTO;
import com.example.SP26SE025.entity.Notification;
import com.example.SP26SE025.entity.ServicePackage;
import com.example.SP26SE025.entity.Subscription; // Import Subscription Entity
import com.example.SP26SE025.entity.User;
import com.example.SP26SE025.repository.ServicePackageRepository;
import com.example.SP26SE025.repository.SubscriptionRepository; // Import Subscription Repository
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
import java.time.LocalDateTime; // Import thời gian
import java.util.ArrayList;
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

    @Autowired
    private ServicePackageRepository packageRepository;

    // 1. Inject SubscriptionRepository để lưu lịch sử mua
    @Autowired
    private SubscriptionRepository subscriptionRepository;

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

    // --- 2. QUẢN LÝ HỒ SƠ ---
    @GetMapping("/customer/profile")
    public String showProfile(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByEmail(auth.getName());
        
        if (user == null) {
            user = new User();
            user.setFullName("Người dùng mới");
            user.setEmail(auth.getName());
        }

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
        if (result.hasErrors()) {
            model.addAttribute("userProfile", userProfile);
            return "customer/profile";
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByEmail(auth.getName());
        
        if (user != null) {
            if (avatarFile != null && !avatarFile.isEmpty()) {
                try {
                    String avatarPath = uploadAvatar(avatarFile, user.getId());
                    user.setAvatarPath(avatarPath);
                } catch (Exception e) {
                    System.out.println("Lỗi upload avatar: " + e.getMessage());
                }
            }

            user.setFullName(userProfile.getFullName());
            user.setPhoneNumber(userProfile.getPhone());
            user.setDob(userProfile.getDob());
            user.setDiabetesType(userProfile.getDiabetesType());
            user.setHypertension(userProfile.getHypertension());
            user.setMedicalHistory(userProfile.getMedicalHistory());
            
            userService.updateProfile(user.getId(), user);
        }
        return "redirect:/customer/profile?success";
    }

    // GET handler để redirect nếu access trực tiếp
    @GetMapping("/customer/profile/update")
    public String getUpdateProfile() {
        return "redirect:/customer/profile";
    }

    private String uploadAvatar(MultipartFile file, Long userId) throws Exception {
        String uploadDir = System.getProperty("user.dir") + "/target/classes/static/images/uploads";
        File uploadFolder = new File(uploadDir);
        if (!uploadFolder.exists()) uploadFolder.mkdirs();

        String originalFilename = file.getOriginalFilename();
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String newFilename = userId + "_" + UUID.randomUUID().toString() + fileExtension;
        
        File destFile = new File(uploadDir, newFilename);
        file.transferTo(destFile);
        
        return "/images/uploads/" + newFilename;
    }

    // --- 3. THÔNG BÁO ---
    @GetMapping("/customer/notifications")
    public String showNotifications(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByEmail(auth.getName());
        
        if (user != null) {
            List<Notification> notifs = notificationService.getAllNotifications(user);
            List<NotificationDTO> notifDTOs = notifs.stream()
                .map(notif -> new NotificationDTO(notif.getTitle(), notif.getMessage(), notif.getType(), notif.getCreatedAt(), notif.isRead()))
                .collect(Collectors.toList());
            model.addAttribute("notifications", notifDTOs);
        } else {
            model.addAttribute("notifications", new ArrayList<>());
        }
        return "customer/notifications";
    }
    
    // ==========================================
    // --- 4. GÓI DỊCH VỤ (PACKAGES) - ĐÃ CẬP NHẬT ---
    // ==========================================

    @GetMapping("/customer/packages")
    public String showPackagesPage(Model model) {
        // Lấy danh sách gói ĐANG HOẠT ĐỘNG từ DB và truyền vào Model
        model.addAttribute("packages", packageRepository.findByIsActiveTrue());
        return "customer/packages"; 
    }

    // --- XỬ LÝ MUA GÓI ---
    @GetMapping("/customer/packages/buy")
    public String buyPackage(@RequestParam(name = "planId") Long planId) {
        
        // 1. Lấy User hiện tại
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userService.findByEmail(auth.getName());

        // 2. Lấy thông tin Gói
        ServicePackage pkg = packageRepository.findById(planId).orElse(null);

        if (currentUser != null && pkg != null) {
            // 3. Tạo bản ghi Đăng ký (Subscription)
            Subscription sub = new Subscription();
            sub.setUser(currentUser);
            sub.setPlanName(pkg.getPackageName()); // Lưu tên gói tại thời điểm mua
            sub.setPrice(pkg.getPrice());          // Lưu giá tại thời điểm mua
            sub.setStartDate(LocalDateTime.now());
            sub.setStatus("ACTIVE"); 

            // Tính ngày hết hạn (Ví dụ đơn giản: Gói năm thì +1 năm, còn lại +1 tháng)
            if (pkg.getPeriod() != null && pkg.getPeriod().toLowerCase().contains("năm")) {
                sub.setEndDate(LocalDateTime.now().plusYears(1));
            } else {
                sub.setEndDate(LocalDateTime.now().plusMonths(1));
            }

            // 4. Lưu vào DB
            subscriptionRepository.save(sub);
            
            System.out.println(">>> Đã lưu đăng ký thành công cho: " + currentUser.getFullName());
        }

        return "redirect:/customer/home?success=bought";
    }

    // --- 5. CÁC TRANG PLACEHOLDER & REDIRECT ---
    @GetMapping("/customer/reports/analysis")
    public String showAnalysis(Model model) { 
        model.addAttribute("historyList", new ArrayList<>());
        return "customer/analysis_report"; 
    }
    
    @GetMapping({"/customer/upload", "/customer/doctor-chat", "/test-services/customer"})
    public String temporaryRedirect() {
        return "redirect:/customer/home";
    }
}