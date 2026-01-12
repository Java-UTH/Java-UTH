package com.example.SP26SE025.controller;

import com.example.SP26SE025.entity.Clinic;
import com.example.SP26SE025.entity.ClinicStatus;
import com.example.SP26SE025.repository.ClinicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/api/clinics")
public class AdminClinicController {

    @Autowired
    private ClinicRepository clinicRepository;

    // 🔹 Lấy tất cả phòng khám
    @GetMapping
    public List<Clinic> getAll() {
        return clinicRepository.findAll();
    }

    // 🔹 Duyệt phòng khám
    @PutMapping("/{id}/approve")
    public ResponseEntity<?> approve(@PathVariable Long id) {
        Clinic clinic = clinicRepository.findById(id).orElseThrow();
        clinic.setStatus(ClinicStatus.APPROVED);
        clinicRepository.save(clinic);
        return ResponseEntity.ok().build();
    }

    // 🔹 Tạm dừng phòng khám
    @PutMapping("/{id}/suspend")
    public ResponseEntity<?> suspend(@PathVariable Long id) {
        Clinic clinic = clinicRepository.findById(id).orElseThrow();
        clinic.setStatus(ClinicStatus.SUSPENDED);
        clinicRepository.save(clinic);
        return ResponseEntity.ok().build();
    }
}

