package com.example.SP26SE025.service;

import com.example.SP26SE025.dtos.DoctorRegistrationDto;
import com.example.SP26SE025.entity.Patient;
import com.example.SP26SE025.entity.Role;
import com.example.SP26SE025.entity.User;
import com.example.SP26SE025.repository.PatientRepository;
import com.example.SP26SE025.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClinicAdminService {

    @Autowired private UserRepository userRepository;
    @Autowired private PatientRepository patientRepository;
    @Autowired private BCryptPasswordEncoder passwordEncoder;

    // 1. Lấy danh sách Bác sĩ (Role = DOCTOR)
    public List<User> getAllDoctors() {
        return userRepository.findByRole(Role.DOCTOR);
    }

    // 2. Lấy danh sách Bệnh nhân (Hồ sơ của CUSTOMER)
    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    // 3. Thêm Bác sĩ mới
    public void createDoctor(DoctorRegistrationDto dto) {
        User doctor = new User();
        doctor.setFullName(dto.getFullName());
        doctor.setUsername(dto.getUsername());
        doctor.setEmail(dto.getEmail());
        doctor.setSpecialist(dto.getSpecialist());
        
        doctor.setPassword(passwordEncoder.encode(dto.getPassword()));
        
        // Gán Role là DOCTOR
        doctor.setRole(Role.DOCTOR);
        doctor.setEnabled(true);

        userRepository.save(doctor);
    }
}