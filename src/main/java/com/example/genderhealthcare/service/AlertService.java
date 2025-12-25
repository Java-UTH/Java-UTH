package com.example.SP26SE025.service;

import com.example.SP26SE025.entity.Alert;
import com.example.SP26SE025.repository.AlertRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlertService {

    private final AlertRepository alertRepository;

    // Constructor Injection (khuyên dùng)
    public AlertService(AlertRepository alertRepository) {
        this.alertRepository = alertRepository;
    }

    /**
     * Lấy danh sách cảnh báo của 1 người dùng
     * @param userId id người dùng
     * @return danh sách Alert
     */
    public List<Alert> getAlertsByUserId(Long userId) {
        return alertRepository.findByUserId(userId);
    }

    /**
     * Tạo mới một cảnh báo (dùng khi AI trả kết quả)
     */
    public Alert createAlert(Alert alert) {
        return alertRepository.save(alert);
    }
}
