package com.example.SP26SE025.controller;

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
}
