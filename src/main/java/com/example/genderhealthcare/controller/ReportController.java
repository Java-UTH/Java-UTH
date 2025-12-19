package com.example.genderhealthcare.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.genderhealthcare.dtos.MonthlyCountDTO;
import com.example.genderhealthcare.entity.User;
import com.example.genderhealthcare.service.ReportService;
import com.example.genderhealthcare.service.UserService;

import java.security.Principal;
import java.util.List;

@Controller
public class ReportController {

    @Autowired
    private ReportService reportService;

    @Autowired
    private UserService userService;

    @GetMapping("/admin/reports/monthly")
    public String viewMonthlyReports(Model model) {
        List<MonthlyCountDTO> stiStats = reportService.getSTITestCountsByMonth();
        List<MonthlyCountDTO> consStats = reportService.getConsultationCountsByMonth();
        model.addAttribute("stiTestStats", stiStats);
        model.addAttribute("consultationStats", consStats);
        return "admin/report/monthlyReport";
    }

    @GetMapping("/customer/reports/analysis")
    public String viewUserAnalysisHistory(Model model, Principal principal) {
        if (principal != null) {
            User currentUser = userService.findByUsername(principal.getName());
            if (currentUser != null) {
                model.addAttribute("historyList", reportService.getHistory(currentUser));
            }
        }
        return "customer/analysis_report"; 
    }

    @PostMapping("/customer/reports/upload")
    public String handleImageUpload(@RequestParam("file") MultipartFile file,
                                    Principal principal,
                                    RedirectAttributes redirectAttributes) {
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Vui lòng chọn file!");
            return "redirect:/customer/reports/analysis";
        }
        try {
            if (principal != null) {
                User currentUser = userService.findByUsername(principal.getName());
                reportService.saveAnalysis(file, currentUser);
                redirectAttributes.addFlashAttribute("success", "Thành công!");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi: " + e.getMessage());
        }
        return "redirect:/customer/reports/analysis";
    }
}