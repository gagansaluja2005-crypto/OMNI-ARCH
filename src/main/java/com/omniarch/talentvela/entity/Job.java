package com.omniarch.talentvela.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "JOB")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // References EmployerProfile.id
    @Column(name = "employer_id", nullable = false)
    private Integer employerId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(name = "required_skills", columnDefinition = "TEXT")
    private String requiredSkills;

    @Column(name = "experience_required")
    private String experienceRequired;

    @Column(name = "salary_min")
    private BigDecimal salaryMin;

    @Column(name = "salary_max")
    private BigDecimal salaryMax;

    @Column(name = "job_type")
    private String jobType;

    @Column(name = "work_mode")
    private String workMode;

    private Integer openings;

    @Column(name = "application_deadline")
    private LocalDate applicationDeadline;

    @Column(name = "location_address")
    private String locationAddress;

    @Column(name = "location_city")
    private String locationCity;

    @Column(name = "location_state")
    private String locationState;

    @Column(name = "location_country")
    private String locationCountry;

    // DRAFT, PENDING, APPROVED, REJECTED, CLOSED, PUBLISHED
    @Column(nullable = false)
    private String status = "DRAFT";

    @Column(name = "rejection_reason", columnDefinition = "TEXT")
    private String rejectionReason;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;

    public Integer getId() { return id; }

    public void setId(Integer id) { this.id = id; }

    public Integer getEmployerId() { return employerId; }

    public void setEmployerId(Integer employerId) { this.employerId = employerId; }

    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public String getRequiredSkills() { return requiredSkills; }

    public void setRequiredSkills(String requiredSkills) { this.requiredSkills = requiredSkills; }

    public String getExperienceRequired() { return experienceRequired; }

    public void setExperienceRequired(String experienceRequired) { this.experienceRequired = experienceRequired; }

    public BigDecimal getSalaryMin() { return salaryMin; }

    public void setSalaryMin(BigDecimal salaryMin) { this.salaryMin = salaryMin; }

    public BigDecimal getSalaryMax() { return salaryMax; }

    public void setSalaryMax(BigDecimal salaryMax) { this.salaryMax = salaryMax; }

    public String getJobType() { return jobType; }

    public void setJobType(String jobType) { this.jobType = jobType; }

    public String getWorkMode() { return workMode; }

    public void setWorkMode(String workMode) { this.workMode = workMode; }

    public Integer getOpenings() { return openings; }

    public void setOpenings(Integer openings) { this.openings = openings; }

    public LocalDate getApplicationDeadline() { return applicationDeadline; }

    public void setApplicationDeadline(LocalDate applicationDeadline) { this.applicationDeadline = applicationDeadline; }

    public String getLocationAddress() { return locationAddress; }

    public void setLocationAddress(String locationAddress) { this.locationAddress = locationAddress; }

    public String getLocationCity() { return locationCity; }

    public void setLocationCity(String locationCity) { this.locationCity = locationCity; }

    public String getLocationState() { return locationState; }

    public void setLocationState(String locationState) { this.locationState = locationState; }

    public String getLocationCountry() { return locationCountry; }

    public void setLocationCountry(String locationCountry) { this.locationCountry = locationCountry; }

    public String getStatus() { return status; }

    public void setStatus(String status) { this.status = status; }

    public String getRejectionReason() { return rejectionReason; }

    public void setRejectionReason(String rejectionReason) { this.rejectionReason = rejectionReason; }

    public LocalDateTime getCreatedAt() { return createdAt; }

    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }

    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
