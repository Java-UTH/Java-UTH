package com.example.SP26SE025.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.SP26SE025.entity.Question;
import com.example.SP26SE025.entity.User;
import com.example.SP26SE025.security.CustomUserDetails;
import com.example.SP26SE025.service.QuestionService;

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
