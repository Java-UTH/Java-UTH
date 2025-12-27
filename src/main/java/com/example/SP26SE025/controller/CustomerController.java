package com.example.SP26SE025.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.ArrayList;

@Controller
public class CustomerController {

    @GetMapping("/customer/home")
    public String customerHome() {
        return "customer/home";
    }

    @GetMapping("/customer/reports/analysis") 
    public String showAnalysisPage(Model model) {
        model.addAttribute("historyList", new ArrayList<>());
        return "customer/analysis_report"; 
    }
}