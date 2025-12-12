package com.example.genderhealthcare.repository;

import com.example.genderhealthcare.entity.Question;
import com.example.genderhealthcare.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findByCustomer(User customer);

}
