package com.example.SP26SE025.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.SP26SE025.dtos.MonthlyCountDTO;
import com.example.SP26SE025.repository.ConsultationAppointmentRepository;
import com.example.SP26SE025.repository.STITestRepository;

import java.util.List;

@Service
public class ReportService {

    @Autowired
    private STITestRepository stiTestRepository;

    @Autowired
    private ConsultationAppointmentRepository consultationRepository;

    public List<MonthlyCountDTO> getSTITestCountsByMonth() {
        return stiTestRepository.countSTITestByMonth();
    }

    public List<MonthlyCountDTO> getConsultationCountsByMonth() {
        return consultationRepository.countConsultationByMonth();
    }
}
