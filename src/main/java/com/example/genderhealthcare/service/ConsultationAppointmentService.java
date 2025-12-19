package com.example.SP26SE025.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.SP26SE025.entity.ConsultationAppointment;
import com.example.SP26SE025.entity.User;
import com.example.SP26SE025.repository.ConsultationAppointmentRepository;

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
