package com.example.SP26SE025.controller;

import com.example.SP26SE025.dtos.DoctorRegistrationDto;
import com.example.SP26SE025.service.ClinicAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model; // Nhớ import Model
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/clinic")
public class ClinicController {

    // 1. Inject Service để lấy dữ liệu (Bước 6)
    @Autowired
    private ClinicAdminService clinicAdminService;

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

    // ========================================================================
    // [FR-23] QUẢN LÝ TÀI KHOẢN (ĐÃ CẬP NHẬT LOGIC TỪ BƯỚC 6)
    // ========================================================================
    
    // Hiển thị trang quản lý (Lấy danh sách Bác sĩ/Bệnh nhân)
    @GetMapping("/admin/users")
    public String userManagement(Model model) {
        // Lấy danh sách bác sĩ từ Service đưa vào Model
        model.addAttribute("doctorsList", clinicAdminService.getAllDoctors());
        
        // Lấy danh sách bệnh nhân từ Service đưa vào Model
        model.addAttribute("patientsList", clinicAdminService.getAllPatients());
        
        // Tạo object rỗng để hứng dữ liệu form thêm mới
        model.addAttribute("newDoctor", new DoctorRegistrationDto());
        
        return "clinic/user_management";
    }

    // Xử lý Form thêm bác sĩ mới (POST)
    @PostMapping("/admin/users/add-doctor")
    public String addDoctor(@ModelAttribute("newDoctor") DoctorRegistrationDto doctorDto) {
        // Gọi Service để lưu xuống DB
        clinicAdminService.createDoctor(doctorDto);
        
        // Quay lại trang danh sách và thêm tham số success
        return "redirect:/clinic/admin/users?success";
    }
    // ========================================================================

    @GetMapping("/reports/summary")
    public String showStatistics() {
        return "clinic/statistics";
    }

    //-----Theo dõi Gói dịch vụ & Gia hạn-----//
    @GetMapping("/subscription")
    public String showSubscriptionPage() {
        return "clinic/subscription";
    }

    @GetMapping("/subscription/purchase")
    public String initiatePurchase(@RequestParam String plan) {
        System.out.println("Người dùng muốn mua gói: " + plan);
        return "redirect:/clinic/subscription?success=true";
    }

    // --------- //

    @GetMapping("/settings")
    public String showSettingsPage() {
        return "clinic/clinic_settings";
    }
}