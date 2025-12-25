package com.example.SP26SE025.controller;

import com.example.SP26SE025.entity.Alert;
import com.example.SP26SE025.service.AlertService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alerts")
public class AlertController {

    private final AlertService alertService;

    // Constructor Injection
    public AlertController(AlertService alertService) {
        this.alertService = alertService;
    }

    /**
     * API lấy danh sách cảnh báo của người dùng
     * GET /api/alerts/user/{userId}
     */
    @GetMapping("/user/{userId}")
    public List<Alert> getAlertsByUserId(@PathVariable Long userId) {
        return alertService.getAlertsByUserId(userId);
    }

    /**
     * API tạo cảnh báo mới
     * POST /api/alerts
     */
    @PostMapping
    public Alert createAlert(@RequestBody Alert alert) {
        return alertService.createAlert(alert);
    }
}
