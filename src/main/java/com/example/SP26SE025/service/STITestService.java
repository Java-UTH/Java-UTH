package com.example.SP26SE025.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.SP26SE025.entity.STITest;
import com.example.SP26SE025.entity.User;
import com.example.SP26SE025.repository.STITestRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class STITestService {

    @Autowired
    private STITestRepository stiTestRepository;

    public STITest bookTest(STITest test) {
        return stiTestRepository.save(test);
    }

    public List<STITest> getAllTests() {
        return stiTestRepository.findAll();
    }

    public List<STITest> getTestsByCustomer(User customer) {
        System.out.print("Chayj toi day111");
        return stiTestRepository.findByCustomer(customer);
    }

    public STITest updateResult(Long id, String result, LocalDateTime resultTime) {
        STITest test = stiTestRepository.findById(id).orElse(null);
        if (test != null) {
            test.setResult(result);
            test.setResultTime(resultTime);
            test.setStatus("completed");
            return stiTestRepository.save(test);
        }
        return null;
    }
}
