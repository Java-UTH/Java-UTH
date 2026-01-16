package com.example.SP26SE025.service;

import com.example.SP26SE025.dtos.DoctorRegistrationDto;
import com.example.SP26SE025.dtos.PatientListDto;
import com.example.SP26SE025.entity.Role;
import com.example.SP26SE025.entity.Subscription;
import com.example.SP26SE025.entity.User;
import com.example.SP26SE025.repository.SubscriptionRepository;
import com.example.SP26SE025.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors; 

@Service
public class ClinicAdminService {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // --- (Giữ nguyên các hàm quản lý Bác sĩ) ---
    public List<User> getAllDoctors() {
        return userRepository.findByRole(Role.DOCTOR);
    }

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

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

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
    // PHẦN 2: QUẢN LÝ BỆNH NHÂN - HIỂN THỊ NHIỀU GÓI
    // ==========================================================

    public List<PatientListDto> getAllPatients(String keyword) {
        List<User> patients = userRepository.findByRole(Role.CUSTOMER);
        List<PatientListDto> dtoList = new ArrayList<>();
        
        String searchKey = (keyword != null) ? keyword.toLowerCase().trim() : "";

        for (User user : patients) {
            
            // --- LOGIC TÌM KIẾM (Giữ nguyên) ---
            if (!searchKey.isEmpty()) {
                String displayId = "bn-" + user.getId();
                String fullName = (user.getFullName() != null) ? user.getFullName().toLowerCase() : "";
                String phone = (user.getPhoneNumber() != null) ? user.getPhoneNumber() : "";
                
                boolean matchId = displayId.contains(searchKey);
                boolean matchName = fullName.contains(searchKey);
                boolean matchPhone = phone.contains(searchKey);

                if (!matchId && !matchName && !matchPhone) {
                    continue; 
                }
            }

            // --- MAP SANG DTO ---
            PatientListDto dto = new PatientListDto();
            dto.setId(user.getId());
            dto.setFullName(user.getFullName());
            dto.setEmail(user.getEmail());
            dto.setPhoneNumber(user.getPhoneNumber());

            // -----------------------------------------------------------
            // [CẬP NHẬT] LOGIC HIỂN THỊ NHIỀU GÓI (Vd: VIP, Cơ bản)
            // -----------------------------------------------------------
            
            String packageName = "Chưa đăng ký";
            String statusClass = "text-secondary"; 

            // 1. Lấy danh sách tất cả các gói của user này
            List<Subscription> subs = subscriptionRepository.findByUserIdOrderByIdDesc(user.getId());

            if (!subs.isEmpty()) {
                // 2. Lấy tên các gói và nối lại bằng dấu phẩy
                // Vd: ["Gói VIP", "Gói Gym"] -> "Gói VIP, Gói Gym"
                List<String> names = new ArrayList<>();
                
                // Mặc định lấy class màu của gói mới nhất (gói đầu tiên)
                String firstPlanName = subs.get(0).getPlanName();
                if (firstPlanName != null) {
                     String lowerName = firstPlanName.toLowerCase();
                     if (lowerName.contains("vip") || lowerName.contains("cao cấp")) {
                         statusClass = "text-warning fw-bold"; 
                     } else if (lowerName.contains("cơ bản") || lowerName.contains("thường")) {
                         statusClass = "text-primary fw-bold"; 
                     } else {
                         statusClass = "text-success fw-bold"; 
                     }
                }

                // Duyệt qua danh sách để lấy tên (Tối đa hiển thị 3 gói cho đỡ dài)
                for (int i = 0; i < subs.size(); i++) {
                    if (i >= 3) { 
                        names.add("..."); // Nếu nhiều quá thì hiện dấu ...
                        break; 
                    }
                    Subscription s = subs.get(i);
                    if (s.getPlanName() != null) {
                        names.add(s.getPlanName());
                    }
                }
                
                // Nối chuỗi
                if (!names.isEmpty()) {
                    packageName = String.join(", ", names);
                }
            }

            dto.setSubscriptionPlan(packageName);
            dto.setStatusClass(statusClass);

            dtoList.add(dto);
        }
        return dtoList;
    }
}