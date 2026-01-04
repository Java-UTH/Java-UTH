package com.example.SP26SE025.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.SP26SE025.entity.AnalysisRecord;
import com.example.SP26SE025.repository.AnalysisRecordRepository;

@Service
public class DoctorService {

    @Autowired
    private AnalysisRecordRepository analysisRepo;

    // 1️⃣ Danh sách ca của doctor
    public List<AnalysisRecord> getAnalysesByDoctor(Long doctorId) {
        return analysisRepo.findByDoctorId(doctorId);
    }

    // 2️⃣ Xem chi tiết 1 ca
    public AnalysisRecord getAnalysisDetail(Long id) {
        return analysisRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Analysis not found"));
    }

    // 3️⃣ Doctor review AI
    public AnalysisRecord reviewAnalysis(
            Long id,
            String doctorNote,
            String riskLevel) {

        AnalysisRecord record = getAnalysisDetail(id);
        record.setDoctorNote(doctorNote);
        record.setRiskLevel(riskLevel);
        record.setStatus("REVIEWED");

        return analysisRepo.save(record);
    }
}
