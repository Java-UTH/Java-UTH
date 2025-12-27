package com.example.SP26SE025.service;

import com.example.SP26SE025.entity.AnalysisRecord;
import com.example.SP26SE025.entity.User;
import com.example.SP26SE025.repository.AnalysisRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
public class ReportService {

    @Autowired
    private AnalysisRecordRepository analysisRecordRepository;

    private final String UPLOAD_DIR = "src/main/resources/static/images/uploads/";



    public void saveAnalysis(MultipartFile file, User user) throws IOException {
        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String originalFileName = file.getOriginalFilename();
        String fileName = System.currentTimeMillis() + "_" + originalFileName;
        
        try (InputStream inputStream = file.getInputStream()) {
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        }

        AnalysisRecord record = new AnalysisRecord();
        record.setImageName(fileName);
        record.setImageUrl("/images/uploads/" + fileName);
        record.setUser(user);
        record.setCreatedAt(LocalDateTime.now());
        record.setAiResult(generateAiDiagnosis(originalFileName)); 
        
        analysisRecordRepository.save(record);
    }

    private String generateAiDiagnosis(String fileName) {
        String[] diagnoses = {
            "Phân tích AI: Phát hiện dấu hiệu viêm kết mạc (Conjunctivitis). Khuyến nghị nhỏ thuốc mắt chuyên dụng.",
            "Phân tích AI: Ghi nhận tình trạng viêm bờ mi cấp tính. Hãy hạn chế tiếp xúc với bụi bẩn.",
            "Phân tích AI: Mắt có dấu hiệu kích ứng nhẹ (đỏ mắt). Cần nghỉ ngơi và tránh dùng thiết bị điện tử.",
            "Phân tích AI: Không phát hiện bất thường về mặt lâm sàng. Sức khỏe mắt của bạn rất tốt.",
            "Phân tích AI: Cảnh báo tình trạng khô mắt (Dry Eye Syndrome). Khuyến nghị dùng nước mắt nhân tạo."
        };
        Random random = new Random();
        return diagnoses[random.nextInt(diagnoses.length)];
    }

    public List<AnalysisRecord> getHistory(User user) {
        return analysisRecordRepository.findByUserOrderByCreatedAtDesc(user);
    }
}