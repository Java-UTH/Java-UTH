package com.example.SP26SE025.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.SP26SE025.entity.ConsultationAppointment;
import com.example.SP26SE025.entity.User;
import com.example.SP26SE025.security.CustomUserDetails;
import com.example.SP26SE025.service.ConsultationAppointmentService;

@Controller
@RequestMapping("/customer/consultation")
public class ConsultationController {

    @Autowired
    private ConsultationAppointmentService appointmentService;

    @PostMapping("/book")
    public String bookAppointment(@ModelAttribute ConsultationAppointment appointment, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = userDetails.getUser();
        appointment.setCustomer(user);
        appointmentService.saveBookAppointment(appointment);
        model.addAttribute("name", user.getFullName());
        model.addAttribute("message", "Đặt lịch thành công!");
        return "customer/consultation_booking";
    }
    @PostMapping("/update")
    public String updateAppointment(@ModelAttribute ConsultationAppointment appointment) {
        appointmentService.updateAppointment(appointment);
        return "redirect:/customer/list";
    }

}
