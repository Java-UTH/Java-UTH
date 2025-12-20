package com.example.SP26SE025.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.SP26SE025.entity.STITest;
import com.example.SP26SE025.service.STITestService;

import java.util.List;

@Controller
@RequestMapping("/staff")
public class StaffController {

    @GetMapping("/home")
    public String home() {
        return "staff/home";
    }

    @Autowired
    private STITestService stiTestService;
    @GetMapping("/all")
    public String viewAllTests(Model model) {
        List<STITest> tests = stiTestService.getAllTests();
        model.addAttribute("tests", tests);
        return "staff/all_sti_tests";
    }
}
