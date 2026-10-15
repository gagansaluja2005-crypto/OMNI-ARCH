package com.omniarch.talentvela.repository;

import com.omniarch.talentvela.entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Integer> {
    List<Application> findByJobId(Integer jobId);
    List<Application> findByJobSeekerId(Integer jobSeekerId);
    
    // Check if a seeker has already applied to a specific job (enforces UNIQUE constraint)
    boolean existsByJobIdAndJobSeekerId(Integer jobId, Integer jobSeekerId);
}
