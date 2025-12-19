package com.example.SP26SE025.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.SP26SE025.entity.ConsultationAppointment;
import com.example.SP26SE025.entity.Question;
import com.example.SP26SE025.entity.STITest;
import com.example.SP26SE025.entity.User;
import com.example.SP26SE025.security.CustomUserDetails;
import com.example.SP26SE025.service.ConsultationAppointmentService;
import com.example.SP26SE025.service.QuestionService;
import com.example.SP26SE025.service.STITestService;

import java.util.List;

@Controller
public class CustomerController {

    private final ConsultationAppointmentService consultationAppointmentService;
    private final QuestionService questionService;
    private final STITestService stiTestService;

    public CustomerController(ConsultationAppointmentService consultationAppointmentService, QuestionService questionService, STITestService stiTestService) {
        this.consultationAppointmentService = consultationAppointmentService;
        this.questionService = questionService;
        this.stiTestService = stiTestService;
    }

    @GetMapping("/customer/home")
    public String customerHome() {
        return "customer/home";
    }

    @GetMapping("/customer/cycle")
    public String menstrualCycle() {
        return "customer/menstrual_cycle";
    }

    @GetMapping("/customer/consultation")
    public String consultationBooking() {
        return "customer/consultation_booking";
    }

    @GetMapping("/customer/form-cycle")
    public String cycle() {
        return "customer/form_cycle";
    }

    @GetMapping("/customer/question_list")
    public String listQuestions(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = userDetails.getUser();
        List<Question> questions = questionService.getQuestionsByCustomer(user);
        model.addAttribute("questions", questions);
        return "customer/question_list";
    }

    @GetMapping("/customer/booking")
    public String showBookingForm(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = userDetails.getUser();
        model.addAttribute("appointment", new ConsultationAppointment());
        model.addAttribute("name", user.getFullName());
        return "customer/booking";
    }

    @GetMapping("/customer/list")
    public String viewAppointments(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = userDetails.getUser();
        List<ConsultationAppointment> appointments = consultationAppointmentService.getAppointmentsByCustomerId(user.getId());
        model.addAttribute("appointments", appointments);
        return "customer/consultation_list";
    }

    @GetMapping("/edit")
    public String editForm(@RequestParam Long id, Model model) {
        ConsultationAppointment appointment = consultationAppointmentService.getAppointmentById(id);
        model.addAttribute("appointment", appointment);
        return "customer/edit_appointment";
    }

    @GetMapping("/delete")
    public String deleteAppointment(@RequestParam Long id) {
        consultationAppointmentService.deleteAppointment(id);
        return "redirect:/customer/list";
    }

    @GetMapping("/ask")
    public String showForm(Model model) {
        model.addAttribute("question", new Question());
        return "customer/ask_question";
    }

    @GetMapping("/list")
    public String viewQuestions(Model model, HttpSession session) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = userDetails.getUser();
        List<Question> questions = questionService.getQuestionsByCustomer(user);
        model.addAttribute("questions", questions);
        return "customer/question_list";
    }

    @GetMapping("/customer/question1")
    public String question() {
        return "customer/question";
    }


    @GetMapping("/book")
    public String showBookingForm_sti(Model model) {
        model.addAttribute("stiTest", new STITest());
        return "customer/book_sti";
    }


    @GetMapping("/my-tests")
    public String viewMyTests(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        User customer = userDetails.getUser();

        List<STITest> tests = stiTestService.getTestsByCustomer(customer);
        model.addAttribute("tests", tests);
        return "customer/my_sti_tests";
    }

}
