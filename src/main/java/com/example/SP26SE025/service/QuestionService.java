package com.example.SP26SE025.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.SP26SE025.entity.Question;
import com.example.SP26SE025.entity.User;
import com.example.SP26SE025.repository.QuestionRepository;

import java.util.List;

@Service
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    public Question save(Question question) {
        return questionRepository.save(question);
    }

    public List<Question> getByCustomer(User customer) {
        return questionRepository.findByCustomer(customer);
    }

    public List<Question> getAllQuestions() {
        return questionRepository.findAll();
    }
    public List<Question> getQuestionsByCustomer(User customer) {
        return questionRepository.findByCustomer(customer);
    }

    public Question getById(Long id) {
        return questionRepository.findById(id).orElse(null);
    }


}
