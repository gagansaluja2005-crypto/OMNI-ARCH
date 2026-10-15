package com.omniarch.talentvela.repository;

import com.omniarch.talentvela.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Integer> {
    
    // Find all jobs posted by a specific employer
    List<Job> findByEmployerId(Integer employerId);
    
    // Find all jobs by a specific status (e.g., PUBLISHED, PENDING)
    List<Job> findByStatus(String status);
}
