package com.example.SP26SE025.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.SP26SE025.dtos.MonthlyCountDTO;
import com.example.SP26SE025.service.ReportService;

import java.util.List;

@Controller
@RequestMapping("/admin/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping("/monthly")
    public String viewMonthlyReports(Model model) {
        List<MonthlyCountDTO> stiStats = reportService.getSTITestCountsByMonth();
        List<MonthlyCountDTO> consStats =reportService.getConsultationCountsByMonth();

        System.out.println("STI Stats: " + stiStats.size());
        System.out.println("Consultation Stats: " + consStats.size());

        System.out.println("📋 STI Stats:");
        for (MonthlyCountDTO stat : stiStats) {
            System.out.println(" - Tháng: " + stat.getMonth() + " | Số lượt: " + stat.getCount());
        }

        System.out.println("📋 Consultation Stats:");
        for (MonthlyCountDTO stat : consStats) {
            System.out.println(" - Tháng: " + stat.getMonth() + " | Số lượt: " + stat.getCount());
        }

        model.addAttribute("stiTestStats", stiStats);
        model.addAttribute("consultationStats", consStats);
        return "admin/report/monthlyReport";
    }

}
