package com.omniarch.talentvela.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Job entity matching the authoritative schema (JOB table).
 */
public class Job {
    private Integer id;
    private Integer employerId;
    private String companyName;
    private String companyLogo;
    private String title;
    private String description;
    private String requiredSkills;
    private String experienceRequired;
    private Double salaryMin;
    private Double salaryMax;
    private String jobType;       // e.g. Full-Time, Part-Time, Internship
    private String workMode;      // e.g. Remote, Hybrid, On-Site
    private Integer openings;
    private String applicationDeadline;
    private String locationAddress;
    private String locationCity;
    private String locationState;
    private String locationCountry;
    private String status;         // DRAFT, PENDING, APPROVED, PUBLISHED, CLOSED
    
    // Auxiliary fields for Job Seeker UI and Matching Differentiator
    private Integer matchScore;
    private List<String> matchedSkills = new ArrayList<>();
    private List<String> missingSkills = new ArrayList<>();

    public Job() {}

    public Job(Integer id, Integer employerId, String companyName, String title, 
               String description, String requiredSkills, String experienceRequired, 
               Double salaryMin, Double salaryMax, String jobType, String workMode, 
               Integer openings, String applicationDeadline, String locationCity, 
               String locationState, String locationCountry, String status) {
        this.id = id;
        this.employerId = employerId;
        this.companyName = companyName;
        this.title = title;
        this.description = description;
        this.requiredSkills = requiredSkills;
        this.experienceRequired = experienceRequired;
        this.salaryMin = salaryMin;
        this.salaryMax = salaryMax;
        this.jobType = jobType;
        this.workMode = workMode;
        this.openings = openings;
        this.applicationDeadline = applicationDeadline;
        this.locationCity = locationCity;
        this.locationState = locationState;
        this.locationCountry = locationCountry;
        this.status = status;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getEmployerId() { return employerId; }
    public void setEmployerId(Integer employerId) { this.employerId = employerId; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public String getCompanyLogo() { return companyLogo; }
    public void setCompanyLogo(String companyLogo) { this.companyLogo = companyLogo; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getRequiredSkills() { return requiredSkills; }
    public void setRequiredSkills(String requiredSkills) { this.requiredSkills = requiredSkills; }

    public String getExperienceRequired() { return experienceRequired; }
    public void setExperienceRequired(String experienceRequired) { this.experienceRequired = experienceRequired; }

    public Double getSalaryMin() { return salaryMin; }
    public void setSalaryMin(Double salaryMin) { this.salaryMin = salaryMin; }

    public Double getSalaryMax() { return salaryMax; }
    public void setSalaryMax(Double salaryMax) { this.salaryMax = salaryMax; }

    public String getJobType() { return jobType; }
    public void setJobType(String jobType) { this.jobType = jobType; }

    public String getWorkMode() { return workMode; }
    public void setWorkMode(String workMode) { this.workMode = workMode; }

    public Integer getOpenings() { return openings; }
    public void setOpenings(Integer openings) { this.openings = openings; }

    public String getApplicationDeadline() { return applicationDeadline; }
    public void setApplicationDeadline(String applicationDeadline) { this.applicationDeadline = applicationDeadline; }

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

    public Integer getMatchScore() { return matchScore; }
    public void setMatchScore(Integer matchScore) { this.matchScore = matchScore; }

    public List<String> getMatchedSkills() { return matchedSkills; }
    public void setMatchedSkills(List<String> matchedSkills) { this.matchedSkills = matchedSkills; }

    public List<String> getMissingSkills() { return missingSkills; }
    public void setMissingSkills(List<String> missingSkills) { this.missingSkills = missingSkills; }

    public String getFormattedSalary() {
        if (salaryMin == null && salaryMax == null) return "Competitive";
        if (salaryMin != null && salaryMax != null) {
            return String.format("₹%,.0f - ₹%,.0f / yr", salaryMin, salaryMax);
        }
        return String.format("Up to ₹%,.0f / yr", salaryMax != null ? salaryMax : salaryMin);
    }

    public String[] getSkillsArray() {
        if (requiredSkills == null || requiredSkills.isBlank()) return new String[0];
        return requiredSkills.split("\\s*,\\s*");
    }

    public boolean isDeadlinePassed() {
        if (applicationDeadline == null || applicationDeadline.isBlank()) return false;
        try {
            return java.time.LocalDate.parse(applicationDeadline).isBefore(java.time.LocalDate.now());
        } catch (Exception e) {
            return false;
        }
    }
}
