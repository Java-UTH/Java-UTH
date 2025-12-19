package com.example.SP26SE025.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.SP26SE025.entity.Question;
import com.example.SP26SE025.entity.User;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findByCustomer(User customer);

}
