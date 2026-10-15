package com.omniarch.talentvela.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "JOB_SEEKER_PROFILE")
@Data 
@NoArgsConstructor 
@AllArgsConstructor
public class JobSeekerProfile {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(name = "user_id", nullable = false, unique = true)
    private Integer userId;
    
    @Column(name = "profile_photo")
    private String profilePhoto;
    
    private String headline;
    
    @Column(columnDefinition = "TEXT")
    private String bio;
    
    private String city;
    private String state;
    private String country;
    
    @Column(name = "desired_role")
    private String desiredRole;
    
    @Column(name = "experience_years")
    private Integer experienceYears;
    
    @Column(columnDefinition = "TEXT")
    private String skills;
    
    @Column(name = "highest_qualification")
    private String highestQualification;
    
    @Column(name = "college_university")
    private String collegeUniversity;
    
    @Column(name = "field_of_study")
    private String fieldOfStudy;
    
    @Column(name = "graduation_year")
    private Integer graduationYear;
    
    @Column(name = "preferred_location")
    private String preferredLocation;
    
    @Column(name = "preferred_work_mode")
    private String preferredWorkMode;
    
    @Column(name = "expected_salary")
    private String expectedSalary;
    
    @Column(name = "notice_period")
    private String noticePeriod;

    public Integer getId() { return id; }

    public void setId(Integer id) { this.id = id; }

    public Integer getUserId() { return userId; }

    public void setUserId(Integer userId) { this.userId = userId; }

    public String getProfilePhoto() { return profilePhoto; }

    public void setProfilePhoto(String profilePhoto) { this.profilePhoto = profilePhoto; }

    public String getHeadline() { return headline; }

    public void setHeadline(String headline) { this.headline = headline; }

    public String getBio() { return bio; }

    public void setBio(String bio) { this.bio = bio; }

    public String getCity() { return city; }

    public void setCity(String city) { this.city = city; }

    public String getState() { return state; }

    public void setState(String state) { this.state = state; }

    public String getCountry() { return country; }

    public void setCountry(String country) { this.country = country; }

    public String getDesiredRole() { return desiredRole; }

    public void setDesiredRole(String desiredRole) { this.desiredRole = desiredRole; }

    public Integer getExperienceYears() { return experienceYears; }

    public void setExperienceYears(Integer experienceYears) { this.experienceYears = experienceYears; }

    public String getSkills() { return skills; }

    public void setSkills(String skills) { this.skills = skills; }

    public String getHighestQualification() { return highestQualification; }

    public void setHighestQualification(String highestQualification) { this.highestQualification = highestQualification; }

    public String getCollegeUniversity() { return collegeUniversity; }

    public void setCollegeUniversity(String collegeUniversity) { this.collegeUniversity = collegeUniversity; }

    public String getFieldOfStudy() { return fieldOfStudy; }

    public void setFieldOfStudy(String fieldOfStudy) { this.fieldOfStudy = fieldOfStudy; }

    public Integer getGraduationYear() { return graduationYear; }

    public void setGraduationYear(Integer graduationYear) { this.graduationYear = graduationYear; }

    public String getPreferredLocation() { return preferredLocation; }

    public void setPreferredLocation(String preferredLocation) { this.preferredLocation = preferredLocation; }

    public String getPreferredWorkMode() { return preferredWorkMode; }

    public void setPreferredWorkMode(String preferredWorkMode) { this.preferredWorkMode = preferredWorkMode; }

    public String getExpectedSalary() { return expectedSalary; }

    public void setExpectedSalary(String expectedSalary) { this.expectedSalary = expectedSalary; }

    public String getNoticePeriod() { return noticePeriod; }

    public void setNoticePeriod(String noticePeriod) { this.noticePeriod = noticePeriod; }
}
