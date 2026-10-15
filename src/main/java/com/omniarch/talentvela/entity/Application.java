package com.omniarch.talentvela.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "APPLICATION")
@Data 
@NoArgsConstructor 
@AllArgsConstructor
public class Application {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(name = "job_id", nullable = false)
    private Integer jobId;
    
    @Column(name = "job_seeker_id", nullable = false)
    private Integer jobSeekerId;
    
    @Column(name = "resume_id")
    private Integer resumeId;
    
    @Column(name = "cover_letter_text", columnDefinition = "TEXT")
    private String coverLetterText;
    
    @Column(name = "cover_letter_path")
    private String coverLetterPath;
    
    @Column(name = "status")
    private String status = "APPLIED";
    
    @Column(name = "rejection_reason", columnDefinition = "TEXT")
    private String rejectionReason;
    
    @Column(name = "applied_at", insertable = false, updatable = false)
    private LocalDateTime appliedAt;
    
    @Column(name = "status_updated_at")
    private LocalDateTime statusUpdatedAt;

    public Integer getId() { return id; }

    public void setId(Integer id) { this.id = id; }

    public Integer getJobId() { return jobId; }

    public void setJobId(Integer jobId) { this.jobId = jobId; }

    public Integer getJobSeekerId() { return jobSeekerId; }

    public void setJobSeekerId(Integer jobSeekerId) { this.jobSeekerId = jobSeekerId; }

    public Integer getResumeId() { return resumeId; }

    public void setResumeId(Integer resumeId) { this.resumeId = resumeId; }

    public String getCoverLetterText() { return coverLetterText; }

    public void setCoverLetterText(String coverLetterText) { this.coverLetterText = coverLetterText; }

    public String getCoverLetterPath() { return coverLetterPath; }

    public void setCoverLetterPath(String coverLetterPath) { this.coverLetterPath = coverLetterPath; }

    public String getStatus() { return status; }

    public void setStatus(String status) { this.status = status; }

    public String getRejectionReason() { return rejectionReason; }

    public void setRejectionReason(String rejectionReason) { this.rejectionReason = rejectionReason; }

    public LocalDateTime getAppliedAt() { return appliedAt; }

    public void setAppliedAt(LocalDateTime appliedAt) { this.appliedAt = appliedAt; }

    public LocalDateTime getStatusUpdatedAt() { return statusUpdatedAt; }

    public void setStatusUpdatedAt(LocalDateTime statusUpdatedAt) { this.statusUpdatedAt = statusUpdatedAt; }
}
