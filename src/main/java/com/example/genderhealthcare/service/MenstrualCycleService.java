package com.example.genderhealthcare.service;

import com.example.genderhealthcare.entity.MenstrualCycle;
import com.example.genderhealthcare.repository.MenstrualCycleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MenstrualCycleService {

    private final MenstrualCycleRepository cycleRepository;

    @Autowired
    public MenstrualCycleService(MenstrualCycleRepository cycleRepository) {
        this.cycleRepository = cycleRepository;
    }

    public MenstrualCycle save(MenstrualCycle cycle) {
        return cycleRepository.save(cycle);
    }

    public List<MenstrualCycle> getByUserId(Long userId) {
        return cycleRepository.findByUserId(userId);
    }

    public Map<String, LocalDate> calculateImportantDates(MenstrualCycle cycle) {
        Map<String, LocalDate> result = new HashMap<>();

        LocalDate ovulationDay = cycle.getStartDate().plusDays(cycle.getCycleLength() - 14); // Ngày rụng trứng
        result.put("Ovulation Day", ovulationDay);
        result.put("Fertile Start", ovulationDay.minusDays(4));
        result.put("Fertile End", ovulationDay.plusDays(1));
        result.put("Next Period Start", cycle.getStartDate().plusDays(cycle.getCycleLength()));
        result.put("Take Pill Reminder", cycle.getStartDate().minusDays(1)); // Giả định uống thuốc 1 ngày trước

        return result;
    }
}
