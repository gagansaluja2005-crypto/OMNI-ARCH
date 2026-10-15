package com.omniarch.talentvela.model;

/**
 * Represents Job Seeker Profile matching USER and JOB_SEEKER_PROFILE tables.
 */
public class JobSeekerProfile {
    private Integer id;
    private Integer userId;
    private String fullName;
    private String email;
    private String phone;
    private String profilePhoto;
    private String headline;
    private String bio;
    private String city;
    private String state;
    private String country;
    private String desiredRole;
    private Integer experienceYears;
    private String skills;
    private String highestQualification;
    private String collegeUniversity;
    private String fieldOfStudy;
    private Integer graduationYear;
    private String preferredLocation;
    private String preferredWorkMode;
    private String expectedSalary;
    private String noticePeriod;

    public JobSeekerProfile() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

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

    public String[] getSkillsArray() {
        if (skills == null || skills.isBlank()) return new String[0];
        return skills.split("\\s*,\\s*");
    }

    public int getProfileCompletionPercentage() {
        int total = 10;
        int completed = 0;
        if (fullName != null && !fullName.isBlank()) completed++;
        if (email != null && !email.isBlank()) completed++;
        if (headline != null && !headline.isBlank()) completed++;
        if (bio != null && !bio.isBlank()) completed++;
        if (city != null && !city.isBlank()) completed++;
        if (desiredRole != null && !desiredRole.isBlank()) completed++;
        if (skills != null && !skills.isBlank()) completed++;
        if (highestQualification != null && !highestQualification.isBlank()) completed++;
        if (expectedSalary != null && !expectedSalary.isBlank()) completed++;
        if (experienceYears != null) completed++;
        return (completed * 100) / total;
    }
}
