package com.example.genderhealthcare.service;

import com.example.genderhealthcare.dtos.MonthlyCountDTO;
import com.example.genderhealthcare.repository.ConsultationAppointmentRepository;
import com.example.genderhealthcare.repository.STITestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
