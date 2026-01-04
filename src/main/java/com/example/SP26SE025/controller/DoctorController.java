package com.example.SP26SE025.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.example.SP26SE025.entity.AnalysisRecord;
import com.example.SP26SE025.entity.User;
import com.example.SP26SE025.service.DoctorService;
import com.example.SP26SE025.dto.ReviewRequest;

@RestController
@RequestMapping("/doctor")
@PreAuthorize("hasRole('DOCTOR')")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    // UI Doctor: danh sách ca
    @GetMapping("/analyses")
    public List<AnalysisRecord> getMyAnalyses(
            @AuthenticationPrincipal User user) {
        return doctorService.getAnalysesByDoctor(user.getId());
    }

    // UI Doctor: chi tiết 1 ca
    @GetMapping("/analysis/{id}")
    public AnalysisRecord getDetail(@PathVariable Long id) {
        return doctorService.getAnalysisDetail(id);
    }

    // UI Doctor: xác nhận AI
    @PostMapping("/analysis/{id}/review")
    public AnalysisRecord review(
            @PathVariable Long id,
            @RequestBody ReviewRequest req) {
        return doctorService.reviewAnalysis(
                id,
                req.getDoctorNote(),
                req.getRiskLevel());
    }
}
