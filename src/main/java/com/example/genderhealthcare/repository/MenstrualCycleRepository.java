package com.example.genderhealthcare.repository;

import com.example.genderhealthcare.entity.MenstrualCycle;
import com.example.genderhealthcare.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

//public interface MenstrualCycleRepository extends JpaRepository<MenstrualCycle, Long> {
//    List<MenstrualCycle> findByUser(User user);
//}
public interface MenstrualCycleRepository extends JpaRepository<MenstrualCycle, Long> {
    List<MenstrualCycle> findByUserId(Long userId);
}