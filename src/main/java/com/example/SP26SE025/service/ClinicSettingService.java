package com.example.SP26SE025.service;

import com.example.SP26SE025.entity.ClinicProfile;
import com.example.SP26SE025.repository.ClinicProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.*;
import java.util.Optional;

@Service
public class ClinicSettingService {

    @Autowired private ClinicProfileRepository clinicProfileRepository;
    // Không cần UserRepository nữa vì ta đã cắt đứt quan hệ

    private final String UPLOAD_DIR = "src/main/resources/static/uploads/verification/";

    // 1. Lấy thông tin Profile (Logic mới: Dựa vào username string)
    public ClinicProfile getProfile(String username) {
        // Tìm xem username này đã có hồ sơ chưa
        Optional<ClinicProfile> existingProfile = clinicProfileRepository.findByUsernameLink(username);

        if (existingProfile.isPresent()) {
            return existingProfile.get();
        } else {
            // Nếu chưa có (ví dụ lần đầu vào bằng clinic@example.com) -> TẠO MỚI LUÔN
            ClinicProfile newProfile = new ClinicProfile();
            newProfile.setUsernameLink(username);
            newProfile.setClinicName("Phòng khám Mới"); // Tên mặc định
            return clinicProfileRepository.save(newProfile);
        }
    }

    // 2. Cập nhật thông tin chung
    public void updateGeneralInfo(String username, ClinicProfile updatedInfo) {
        ClinicProfile profile = getProfile(username);
        profile.setClinicName(updatedInfo.getClinicName());
        profile.setRepresentativeName(updatedInfo.getRepresentativeName());
        profile.setAddress(updatedInfo.getAddress());
        profile.setPhone(updatedInfo.getPhone());
        profile.setWebsite(updatedInfo.getWebsite());
        profile.setDescription(updatedInfo.getDescription());
        clinicProfileRepository.save(profile);
    }

    // 3. Upload hồ sơ
    public void uploadVerificationDocs(String username, String taxId, MultipartFile file1, MultipartFile file2) throws IOException {
        ClinicProfile profile = getProfile(username);
        profile.setTaxId(taxId);
        Files.createDirectories(Paths.get(UPLOAD_DIR));

        if (!file1.isEmpty()) {
            String fileName = System.currentTimeMillis() + "_1_" + file1.getOriginalFilename();
            Files.write(Paths.get(UPLOAD_DIR + fileName), file1.getBytes());
            profile.setBusinessLicenseUrl("/uploads/verification/" + fileName);
        }
        if (!file2.isEmpty()) {
            String fileName = System.currentTimeMillis() + "_2_" + file2.getOriginalFilename();
            Files.write(Paths.get(UPLOAD_DIR + fileName), file2.getBytes());
            profile.setMedicalLicenseUrl("/uploads/verification/" + fileName);
        }
        profile.setVerificationStatus("PENDING");
        clinicProfileRepository.save(profile);
    }
    
    // 4. Đổi pass (Giữ nguyên logic cũ hoặc tạm bỏ qua nếu chưa cần)
    public boolean changePassword(String username, String currentPass, String newPass) {
        return true; // Tạm thời return true để test giao diện
    }
}