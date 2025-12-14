package com.example.SP26SE025.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.SP26SE025.entity.MenstrualCycle;
import com.example.SP26SE025.entity.User;

import java.util.List;

//public interface MenstrualCycleRepository extends JpaRepository<MenstrualCycle, Long> {
//    List<MenstrualCycle> findByUser(User user);
//}
public interface MenstrualCycleRepository extends JpaRepository<MenstrualCycle, Long> {
    List<MenstrualCycle> findByUserId(Long userId);
}