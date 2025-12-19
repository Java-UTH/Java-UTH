package com.example.SP26SE025.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/clinic")
class ClinicController {

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

    @GetMapping("/admin/users")
    public String userManagement() {
        return "clinic/user_management";
    }

    @GetMapping("/reports/summary")
    public String showStatistics() {
        return "clinic/statistics";
    }

    // Mapping giả lập cho nút Xuất CSV (FR-30)
    // @GetMapping("/reports/export")
    // public ResponseEntity<Resource> exportCsv() {
    //     // Tạm thời trả về null hoặc file rỗng để test giao diện không bị lỗi 404
    //     return ResponseEntity.ok().build();
    // }
}
