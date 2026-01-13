package com.example.SP26SE025.repository;

import com.example.SP26SE025.entity.ClinicProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ClinicProfileRepository extends JpaRepository<ClinicProfile, Long> {
    // Tìm theo chuỗi username_link thay vì đối tượng User
    Optional<ClinicProfile> findByUsernameLink(String usernameLink);
}