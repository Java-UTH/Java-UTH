package com.example.SP26SE025.service;

import com.example.SP26SE025.dtos.DoctorRegistrationDto;
import com.example.SP26SE025.dtos.PatientListDto;
import com.example.SP26SE025.entity.Role;
import com.example.SP26SE025.entity.User;
import com.example.SP26SE025.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder; 
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class ClinicAdminService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // ==========================================================
    // PHẦN 1: QUẢN LÝ BÁC SĨ
    // ==========================================================

    // 1. Lấy danh sách Bác sĩ
    public List<User> getAllDoctors() {
        return userRepository.findByRole(Role.DOCTOR);
    }

    // 2. Thêm Bác sĩ mới
    public void createDoctor(DoctorRegistrationDto dto) {
        User doctor = new User();
        doctor.setFullName(dto.getFullName());
        doctor.setUsername(dto.getUsername());
        doctor.setEmail(dto.getEmail());
        doctor.setSpecialist(dto.getSpecialist());
        
        doctor.setPassword(passwordEncoder.encode(dto.getPassword()));
        
        doctor.setRole(Role.DOCTOR);
        doctor.setEnabled(true);

        userRepository.save(doctor);
    }

    // 3. Xóa User (Dùng chung cho cả Bác sĩ và Bệnh nhân)
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    // 4. Cập nhật User (Dùng chung)
    public void updateUser(User user) {
        User existingUser = userRepository.findById(user.getId()).orElse(null);
        if (existingUser != null) {
            existingUser.setFullName(user.getFullName());
            existingUser.setSpecialist(user.getSpecialist());
            existingUser.setPhoneNumber(user.getPhoneNumber());
            existingUser.setEmail(user.getEmail());
            userRepository.save(existingUser);
        }
    }

    // ==========================================================
    // PHẦN 2: QUẢN LÝ BỆNH NHÂN (TÌM KIẾM & DTO)
    // ==========================================================

    /**
     * Lấy danh sách bệnh nhân có hỗ trợ tìm kiếm theo từ khóa
     */
    public List<PatientListDto> getAllPatients(String keyword) {
        // 1. Lấy tất cả user có role là CUSTOMER
        List<User> patients = userRepository.findByRole(Role.CUSTOMER);
        
        List<PatientListDto> dtoList = new ArrayList<>();
        Random rand = new Random();

        // Chuẩn hóa từ khóa tìm kiếm (chữ thường, bỏ khoảng trắng)
        String searchKey = (keyword != null) ? keyword.toLowerCase().trim() : "";

        // 2. Duyệt qua từng User
        for (User user : patients) {
            
            // --- LOGIC TÌM KIẾM ---
            // Nếu có từ khóa tìm kiếm
            if (!searchKey.isEmpty()) {
                String displayId = "bn-" + user.getId();
                String fullName = (user.getFullName() != null) ? user.getFullName().toLowerCase() : "";
                String phone = (user.getPhoneNumber() != null) ? user.getPhoneNumber() : "";
                
                // Kiểm tra xem từ khóa có nằm trong ID, Tên hoặc SĐT không
                boolean matchId = displayId.contains(searchKey);
                boolean matchName = fullName.contains(searchKey);
                boolean matchPhone = phone.contains(searchKey);

                // Nếu không khớp cái nào thì bỏ qua user này
                if (!matchId && !matchName && !matchPhone) {
                    continue; 
                }
            }
            // ---------------------

            // 3. Map sang DTO
            PatientListDto dto = new PatientListDto();
            dto.setId(user.getId());
            dto.setFullName(user.getFullName());
            dto.setEmail(user.getEmail());
            dto.setPhoneNumber(user.getPhoneNumber());

            // 4. Mock Data Gói đăng ký
            int randomType = rand.nextInt(3); 
            if (randomType == 0) {
                dto.setSubscriptionPlan("Gói Miễn phí");
                dto.setStatusClass("text-secondary");
            } else if (randomType == 1) {
                dto.setSubscriptionPlan("Gói Cơ bản (Tháng)");
                dto.setStatusClass("text-primary fw-bold");
            } else {
                dto.setSubscriptionPlan("Gói VIP (Năm)");
                dto.setStatusClass("text-warning fw-bold");
            }

            dtoList.add(dto);
        }
        return dtoList;
    }
}