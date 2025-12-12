package com.example.genderhealthcare.controller;

import com.example.genderhealthcare.service.TestServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WelcomeController {
    @Autowired
    private TestServiceService service;
    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("services", service.findAll());
        return "index";
    }

    @GetMapping("/register")
    public String showRegisterForm() {
        return "register";
    }


}