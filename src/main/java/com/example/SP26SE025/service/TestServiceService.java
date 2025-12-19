package com.example.SP26SE025.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.SP26SE025.entity.TestService;
import com.example.SP26SE025.repository.TestServiceRepository;

import java.util.List;

@Service
public class TestServiceService {

    @Autowired
    private TestServiceRepository repository;

    public List<TestService> findAll() {
        return repository.findAll();
    }

    public TestService save(TestService service) {
        return repository.save(service);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public TestService update (TestService service) {
        TestService entity = repository.findById(service.getId()).get();
        entity.setPrice(service.getPrice());
        return repository.save(service);
    }
}
