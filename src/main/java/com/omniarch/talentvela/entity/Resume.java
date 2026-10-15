package com.omniarch.talentvela.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "RESUME")
@Data 
@NoArgsConstructor 
@AllArgsConstructor
public class Resume {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(name = "job_seeker_id", nullable = false, unique = true)
    private Integer jobSeekerId;
    
    @Column(name = "file_name", nullable = false)
    private String fileName;
    
    @Column(name = "file_path", nullable = false)
    private String filePath;
    
    @Column(name = "file_type")
    private String fileType;
    
    @Column(name = "extracted_text", columnDefinition = "TEXT")
    private String extractedText;
    
    @Column(name = "extracted_skills", columnDefinition = "TEXT")
    private String extractedSkills;
    
    @Column(name = "uploaded_at", insertable = false, updatable = false)
    private LocalDateTime uploadedAt;
    
    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;

    public Integer getId() { return id; }

    public void setId(Integer id) { this.id = id; }

    public Integer getJobSeekerId() { return jobSeekerId; }

    public void setJobSeekerId(Integer jobSeekerId) { this.jobSeekerId = jobSeekerId; }

    public String getFileName() { return fileName; }

    public void setFileName(String fileName) { this.fileName = fileName; }

    public String getFilePath() { return filePath; }

    public void setFilePath(String filePath) { this.filePath = filePath; }

    public String getFileType() { return fileType; }

    public void setFileType(String fileType) { this.fileType = fileType; }

    public String getExtractedText() { return extractedText; }

    public void setExtractedText(String extractedText) { this.extractedText = extractedText; }

    public String getExtractedSkills() { return extractedSkills; }

    public void setExtractedSkills(String extractedSkills) { this.extractedSkills = extractedSkills; }

    public LocalDateTime getUploadedAt() { return uploadedAt; }

    public void setUploadedAt(LocalDateTime uploadedAt) { this.uploadedAt = uploadedAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }

    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
