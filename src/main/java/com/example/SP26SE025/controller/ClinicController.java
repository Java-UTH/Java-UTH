package com.example.SP26SE025.controller;

import com.example.SP26SE025.dtos.DoctorRegistrationDto;
import com.example.SP26SE025.entity.ClinicProfile;
import com.example.SP26SE025.entity.User;
import com.example.SP26SE025.service.ClinicAdminService;
import com.example.SP26SE025.service.ClinicSettingService; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication; 
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException; 

@Controller
@RequestMapping("/clinic")
public class ClinicController {

    @Autowired
    private ClinicAdminService clinicAdminService;

    @Autowired
    private ClinicSettingService clinicSettingService;

    // --- CÁC TRANG CƠ BẢN ---
    @GetMapping("/home")
    public String home() {
        return "clinic/home";
    }

    @GetMapping("/upload")
    public String upload() {
        return "clinic/upload";
    }

    @GetMapping("/reports/patient")
    public String showPatientReports() {
        return "clinic/report_tracking";
    }

    @GetMapping("/reports/summary")
    public String showStatistics() {
        return "clinic/statistics";
    }

    // ========================================================================
    // [FR-23] QUẢN LÝ TÀI KHOẢN (BÁC SĨ / BỆNH NHÂN)
    // ========================================================================
    
    @GetMapping("/admin/users")
    public String userManagement(Model model, 
                                 @RequestParam(value = "keyword", required = false) String keyword,
                                 @RequestParam(value = "tab", defaultValue = "doctors") String activeTab) {
        
        // Logic: Nếu đang tìm kiếm thì tự động chuyển sang tab Bệnh nhân
        if (keyword != null && !keyword.isEmpty()) {
            activeTab = "patients";
        }

        // 1. Lấy danh sách Bác sĩ
        model.addAttribute("doctorsList", clinicAdminService.getAllDoctors());
        
        // 2. Lấy danh sách Bệnh nhân (Có lọc theo từ khóa)
        model.addAttribute("patientsList", clinicAdminService.getAllPatients(keyword));
        
        // 3. Các dữ liệu phụ trợ cho giao diện
        model.addAttribute("newDoctor", new DoctorRegistrationDto());
        model.addAttribute("keyword", keyword); // Để hiện lại chữ vừa gõ trong ô tìm kiếm
        model.addAttribute("activeTab", activeTab); // Để biết tab nào đang sáng

        return "clinic/user_management";
    }

    @PostMapping("/admin/users/add-doctor")
    public String addDoctor(@ModelAttribute("newDoctor") DoctorRegistrationDto doctorDto) {
        clinicAdminService.createDoctor(doctorDto);
        return "redirect:/clinic/admin/users?success";
    }

    // Xử lý Xóa User (Dùng chung cho cả Bác sĩ và Bệnh nhân)
    @GetMapping("/admin/users/delete/{id}")
    public String deleteDoctor(@PathVariable Long id) {
        clinicAdminService.deleteUser(id);
        return "redirect:/clinic/admin/users?deleted";
    }

    // Xử lý Cập nhật User (Dùng chung)
    @PostMapping("/admin/users/update")
    public String updateDoctor(@ModelAttribute User user) {
        clinicAdminService.updateUser(user);
        return "redirect:/clinic/admin/users?updated";
    }

    // ========================================================================
    // GÓI DỊCH VỤ (SUBSCRIPTION)
    // ========================================================================
    
    @GetMapping("/subscription")
    public String showSubscriptionPage() {
        return "clinic/subscription";
    }

    @GetMapping("/subscription/purchase")
    public String initiatePurchase(@RequestParam String plan) {
        System.out.println("Người dùng muốn mua gói: " + plan);
        return "redirect:/clinic/subscription?success=true";
    }

    // ========================================================================
    // [FR-22] THIẾT LẬP PHÒNG KHÁM (SETTINGS)
    // ========================================================================

    @GetMapping("/settings")
    public String showSettingsPage(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        ClinicProfile profile = clinicSettingService.getProfile(auth.getName());
        model.addAttribute("clinicProfile", profile);
        return "clinic/clinic_settings";
    }

    @PostMapping("/settings/update-info")
    public String updateGeneralInfo(@ModelAttribute ClinicProfile clinicProfile) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        clinicSettingService.updateGeneralInfo(auth.getName(), clinicProfile);
        return "redirect:/clinic/settings?success=info";
    }

    @PostMapping("/settings/verify")
    public String uploadVerification(
            @RequestParam("taxId") String taxId,
            @RequestParam("file1") MultipartFile file1,
            @RequestParam("file2") MultipartFile file2) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            clinicSettingService.uploadVerificationDocs(auth.getName(), taxId, file1, file2);
            return "redirect:/clinic/settings?success=verify";
        } catch (IOException e) {
            return "redirect:/clinic/settings?error=upload";
        }
    }

    @PostMapping("/settings/password")
    public String changePassword(
            @RequestParam("currentPass") String currentPass,
            @RequestParam("newPass") String newPass) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean success = clinicSettingService.changePassword(auth.getName(), currentPass, newPass);
        
        if (success) {
            return "redirect:/clinic/settings?success=password";
        } else {
            return "redirect:/clinic/settings?error=password";
        }
    }
}