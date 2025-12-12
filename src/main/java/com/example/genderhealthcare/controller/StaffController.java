package com.example.genderhealthcare.controller;

import com.example.genderhealthcare.entity.STITest;
import com.example.genderhealthcare.service.STITestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
