package com.omniarch.talentvela.model;

/**
 * Represents Resume entity matching RESUME table in schema.
 */
public class Resume {
    private Integer id;
    private Integer jobSeekerId;
    private String fileName;
    private String filePath;
    private String fileType;
    private String extractedText;
    private String extractedSkills;
    private String uploadedAt;

    public Resume() {}

    public Resume(Integer id, Integer jobSeekerId, String fileName, String filePath, 
                  String fileType, String extractedSkills, String uploadedAt) {
        this.id = id;
        this.jobSeekerId = jobSeekerId;
        this.fileName = fileName;
        this.filePath = filePath;
        this.fileType = fileType;
        this.extractedSkills = extractedSkills;
        this.uploadedAt = uploadedAt;
    }

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

    public String getUploadedAt() { return uploadedAt; }
    public void setUploadedAt(String uploadedAt) { this.uploadedAt = uploadedAt; }

    public String[] getExtractedSkillsArray() {
        if (extractedSkills == null || extractedSkills.isBlank()) return new String[0];
        return extractedSkills.split("\\s*,\\s*");
    }
}
