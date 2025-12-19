package com.example.genderhealthcare.service;

import com.example.genderhealthcare.dtos.MonthlyCountDTO;
import com.example.genderhealthcare.entity.AnalysisRecord;
import com.example.genderhealthcare.entity.User;
import com.example.genderhealthcare.repository.AnalysisRecordRepository;
import com.example.genderhealthcare.repository.ConsultationAppointmentRepository;
import com.example.genderhealthcare.repository.STITestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReportService {

    @Autowired
    private STITestRepository stiTestRepository;

    @Autowired
    private ConsultationAppointmentRepository consultationRepository;

    @Autowired
    private AnalysisRecordRepository analysisRecordRepository;

    private final String UPLOAD_DIR = "src/main/resources/static/images/uploads/";

    public List<MonthlyCountDTO> getSTITestCountsByMonth() {
        return stiTestRepository.countSTITestByMonth();
    }

    public List<MonthlyCountDTO> getConsultationCountsByMonth() {
        return consultationRepository.countConsultationByMonth();
    }

    public void saveAnalysis(MultipartFile file, User user) throws IOException {
        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        
        try (InputStream inputStream = file.getInputStream()) {
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        }

        AnalysisRecord record = new AnalysisRecord();
        record.setImageName(fileName);
        record.setImageUrl("/images/uploads/" + fileName);
        record.setUser(user);
        record.setCreatedAt(LocalDateTime.now());
        record.setAiResult("Kết quả phân tích sơ bộ: Bình thường."); 
        
        analysisRecordRepository.save(record);
    }

    public List<AnalysisRecord> getHistory(User user) {
        return analysisRecordRepository.findByUserOrderByCreatedAtDesc(user);
    }
}