package com.example.SP26SE025.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.SP26SE025.service.TestServiceService;

@Controller
public class WelcomeController {

    @Autowired
    private TestServiceService service;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("services", service.findAll());
        return "index";
    }

    // ĐÃ XÓA method /register ĐỂ TRÁNH XUNG ĐỘ
    // Nếu bạn vẫn muốn giữ tạm để test cũ, có thể comment lại:
    // @GetMapping("/old-register")  // đổi đường dẫn tạm thời
    // public String showRegisterForm() {
    //     return "register";
    // }
}