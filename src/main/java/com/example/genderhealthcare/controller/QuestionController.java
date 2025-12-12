package com.example.genderhealthcare.controller;

import com.example.genderhealthcare.entity.Question;
import com.example.genderhealthcare.entity.User;
import com.example.genderhealthcare.security.CustomUserDetails;
import com.example.genderhealthcare.service.QuestionService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/customer/question")
public class QuestionController {

    @Autowired
    private QuestionService questionService;



    @PostMapping("/ask")
    public String submitQuestion(@ModelAttribute Question question) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = userDetails.getUser();

        question.setCustomer(user);
        question.setCreatedAt(LocalDateTime.now());
        questionService.save(question);

        return "redirect:/customer/question_list";
    }



}
