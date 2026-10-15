package com.omniarch.talentvela.model;

/**
 * Represents an Application entity matching APPLICATION table in schema.
 */
public class Application {
    private Integer id;
    private Integer jobId;
    private String jobTitle;
    private String companyName;
    private String location;
    private Integer jobSeekerId;
    private String candidateName;
    private Integer resumeId;
    private String resumeFileName;
    private String coverLetterText;
    private String appliedAt;
    private String status;           // APPLIED, UNDER_REVIEW, SHORTLISTED, REJECTED, SELECTED
    private String statusUpdatedAt;
    private String rejectionReason;

    public Application() {}

    public Application(Integer id, Integer jobId, String jobTitle, String companyName, 
                       String location, Integer jobSeekerId, String candidateName, 
                       Integer resumeId, String resumeFileName, String coverLetterText, 
                       String appliedAt, String status) {
        this.id = id;
        this.jobId = jobId;
        this.jobTitle = jobTitle;
        this.companyName = companyName;
        this.location = location;
        this.jobSeekerId = jobSeekerId;
        this.candidateName = candidateName;
        this.resumeId = resumeId;
        this.resumeFileName = resumeFileName;
        this.coverLetterText = coverLetterText;
        this.appliedAt = appliedAt;
        this.status = status;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getJobId() { return jobId; }
    public void setJobId(Integer jobId) { this.jobId = jobId; }

    public String getJobTitle() { return jobTitle; }
    public void setJobTitle(String jobTitle) { this.jobTitle = jobTitle; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public Integer getJobSeekerId() { return jobSeekerId; }
    public void setJobSeekerId(Integer jobSeekerId) { this.jobSeekerId = jobSeekerId; }

    public String getCandidateName() { return candidateName; }
    public void setCandidateName(String candidateName) { this.candidateName = candidateName; }

    public Integer getResumeId() { return resumeId; }
    public void setResumeId(Integer resumeId) { this.resumeId = resumeId; }

    public String getResumeFileName() { return resumeFileName; }
    public void setResumeFileName(String resumeFileName) { this.resumeFileName = resumeFileName; }

    public String getCoverLetterText() { return coverLetterText; }
    public void setCoverLetterText(String coverLetterText) { this.coverLetterText = coverLetterText; }

    public String getAppliedAt() { return appliedAt; }
    public void setAppliedAt(String appliedAt) { this.appliedAt = appliedAt; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getStatusUpdatedAt() { return statusUpdatedAt; }
    public void setStatusUpdatedAt(String statusUpdatedAt) { this.statusUpdatedAt = statusUpdatedAt; }

    public String getRejectionReason() { return rejectionReason; }
    public void setRejectionReason(String rejectionReason) { this.rejectionReason = rejectionReason; }

    public String getStatusBadgeClass() {
        if (status == null) return "badge-secondary";
        return switch (status) {
            case "APPLIED" -> "badge-info";
            case "UNDER_REVIEW" -> "badge-warning";
            case "SHORTLISTED" -> "badge-primary";
            case "SELECTED" -> "badge-success";
            case "REJECTED" -> "badge-danger";
            default -> "badge-secondary";
        };
    }
}
