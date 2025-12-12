package com.example.genderhealthcare.controller;

import com.example.genderhealthcare.entity.TestService;
import com.example.genderhealthcare.service.TestServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/test-services")
public class TestServiceController {

    @Autowired
    private TestServiceService service;

    @GetMapping("/admin")
    public String listServicesAdmin(Model model) {
        model.addAttribute("services", service.findAll());
        return "admin/test-services/list";
    }

    @GetMapping("/customer")
    public String listServicesCustomer(Model model) {
        model.addAttribute("services", service.findAll());
        return "customer/test-services/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("testService", new TestService());
        return "admin/test-services/form";
    }

    @PostMapping
    public String save(@ModelAttribute TestService testService) {
        service.save(testService);
        return "redirect:/test-services/admin";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        service.delete(id);
        return "redirect:/test-services/admin"; // vẫn giữ redirect
    }

}
