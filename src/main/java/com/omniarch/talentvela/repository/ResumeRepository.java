package com.omniarch.talentvela.repository;

import com.omniarch.talentvela.entity.Resume;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ResumeRepository extends JpaRepository<Resume, Integer> {
    Optional<Resume> findByJobSeekerId(Integer jobSeekerId);
}
