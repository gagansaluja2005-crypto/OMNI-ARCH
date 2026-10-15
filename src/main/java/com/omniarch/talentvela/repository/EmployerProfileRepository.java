package com.omniarch.talentvela.repository;

import com.omniarch.talentvela.entity.EmployerProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployerProfileRepository extends JpaRepository<EmployerProfile, Integer> {
    
    // Custom query to find a profile by the associated User ID
    Optional<EmployerProfile> findByUserId(Integer userId);
}
