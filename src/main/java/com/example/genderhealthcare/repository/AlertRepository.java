package com.example.SP26SE025.repository;

import com.example.SP26SE025.entity.Alert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlertRepository extends JpaRepository<Alert, Long> {

    // Lấy danh sách cảnh báo theo userId
    List<Alert> findByUserId(Long userId);
}
