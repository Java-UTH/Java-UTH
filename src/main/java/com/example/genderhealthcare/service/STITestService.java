package com.example.genderhealthcare.service;

import com.example.genderhealthcare.entity.STITest;
import com.example.genderhealthcare.entity.User;
import com.example.genderhealthcare.repository.STITestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
