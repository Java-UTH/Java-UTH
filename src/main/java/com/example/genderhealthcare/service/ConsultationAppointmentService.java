package com.example.genderhealthcare.service;

import com.example.genderhealthcare.entity.ConsultationAppointment;
import com.example.genderhealthcare.entity.User;
import com.example.genderhealthcare.repository.ConsultationAppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConsultationAppointmentService {
    @Autowired
    ConsultationAppointmentRepository consultationAppointmentRepository;
    @Autowired
    private ConsultationAppointmentRepository repository;

    public List<ConsultationAppointment> getAppointmentsForConsultant(User consultant) {
        return repository.findByConsultant(consultant);
    }

    public ConsultationAppointment updateAppointment(ConsultationAppointment updatedAppointment) {
        ConsultationAppointment existing = consultationAppointmentRepository.getById(updatedAppointment.getId());

        existing.setAppointmentDateTime(updatedAppointment.getAppointmentDateTime());
        existing.setReason(updatedAppointment.getReason());
        existing.setConsultant(updatedAppointment.getConsultant());

        return consultationAppointmentRepository.save(existing);
    }

    public List<ConsultationAppointment> getAllAppointments() {
        return consultationAppointmentRepository.findAll();
    }

    public ConsultationAppointment getAppointmentById(Long id) {
        return consultationAppointmentRepository.findById(id).orElse(null);
    }

    public void deleteAppointment(Long id) {
        consultationAppointmentRepository.deleteById(id);
    }

    public List<ConsultationAppointment> getAppointmentsByCustomerId(Long customerId) {
        return consultationAppointmentRepository.findByCustomer_Id(customerId);
    }

    public void saveBookAppointment(ConsultationAppointment appointment) {
        consultationAppointmentRepository.save(appointment);
    }



}
