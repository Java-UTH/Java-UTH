package com.example.genderhealthcare.controller;

import com.example.genderhealthcare.entity.MenstrualCycle;
import com.example.genderhealthcare.entity.User;
import com.example.genderhealthcare.repository.MenstrualCycleRepository;
import com.example.genderhealthcare.service.MenstrualCycleService;
import com.example.genderhealthcare.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@Controller
@RequestMapping("/customer/cycle")
//@RequiredArgsConstructor
public class MenstrualCycleController {

    private final MenstrualCycleRepository cycleRepository;
    private final MenstrualCycleService cycleService;
    private final UserService userService;

    @Autowired
    public MenstrualCycleController(MenstrualCycleRepository cycleRepository, MenstrualCycleService cycleService, UserService userService) {
        this.cycleRepository = cycleRepository;
        this.cycleService = cycleService;
        this.userService = userService;
    }
    //    @GetMapping("/track")
//    public String showCycleForm(Model model) {
//        model.addAttribute("cycle", new MenstrualCycle());
//        return "customer/track-cycle";
//    }

    @PostMapping("/track")
    public String submitCycle(@ModelAttribute MenstrualCycle cycle, @AuthenticationPrincipal User user, Model model) {
        cycle.setUser(user);
        cycleRepository.save(cycle);

        Map<String, LocalDate> info = cycleService.calculateImportantDates(cycle);
        model.addAttribute("info", info);
        return "customer/cycle-result";
    }
}
