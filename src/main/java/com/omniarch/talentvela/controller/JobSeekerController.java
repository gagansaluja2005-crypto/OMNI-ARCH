package com.omniarch.talentvela.controller;

import com.omniarch.talentvela.model.Application;
import com.omniarch.talentvela.model.Job;
import com.omniarch.talentvela.model.JobSeekerProfile;
import com.omniarch.talentvela.model.Resume;
import com.omniarch.talentvela.entity.User;
import com.omniarch.talentvela.repository.*;
import com.omniarch.talentvela.service.matching.SkillMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class JobSeekerController {

    @Autowired private JobRepository jobRepository;
    @Autowired private ApplicationRepository applicationRepository;
    @Autowired private JobSeekerProfileRepository profileRepository;
    @Autowired private ResumeRepository resumeRepository;
    @Autowired private EmployerProfileRepository employerProfileRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private SkillMatcher skillMatcher;
    
    // Helper to get current userId dynamically from Gagan's SecurityContext with fallback
    private Integer getCurrentUserId() {
        return com.omniarch.talentvela.security.SecurityUtil.getCurrentUserIdOrFallback(4);
    }

    private JobSeekerProfile mapToProfileModel(com.omniarch.talentvela.entity.JobSeekerProfile entityProfile) {
        if (entityProfile == null) return null;
        JobSeekerProfile model = new JobSeekerProfile();
        model.setId(entityProfile.getId());
        model.setUserId(entityProfile.getUserId());
        
        userRepository.findById(entityProfile.getUserId()).ifPresent(user -> {
            model.setFullName(user.getFullName());
            model.setEmail(user.getEmail());
            model.setPhone(user.getPhone());
        });
        
        model.setProfilePhoto(entityProfile.getProfilePhoto());
        model.setHeadline(entityProfile.getHeadline());
        model.setBio(entityProfile.getBio());
        model.setCity(entityProfile.getCity());
        model.setState(entityProfile.getState());
        model.setCountry(entityProfile.getCountry());
        model.setDesiredRole(entityProfile.getDesiredRole());
        model.setExperienceYears(entityProfile.getExperienceYears());
        model.setSkills(entityProfile.getSkills());
        model.setHighestQualification(entityProfile.getHighestQualification());
        model.setCollegeUniversity(entityProfile.getCollegeUniversity());
        model.setFieldOfStudy(entityProfile.getFieldOfStudy());
        model.setGraduationYear(entityProfile.getGraduationYear());
        model.setPreferredLocation(entityProfile.getPreferredLocation());
        model.setPreferredWorkMode(entityProfile.getPreferredWorkMode());
        model.setExpectedSalary(entityProfile.getExpectedSalary());
        model.setNoticePeriod(entityProfile.getNoticePeriod());
        return model;
    }

    private Resume mapToResumeModel(com.omniarch.talentvela.entity.Resume entityResume) {
        if (entityResume == null) return null;
        Resume model = new Resume();
        model.setId(entityResume.getId());
        model.setJobSeekerId(entityResume.getJobSeekerId());
        model.setFileName(entityResume.getFileName());
        model.setFilePath(entityResume.getFilePath());
        model.setFileType(entityResume.getFileType());
        model.setExtractedSkills(entityResume.getExtractedSkills());
        if (entityResume.getUploadedAt() != null) {
            model.setUploadedAt(entityResume.getUploadedAt().toString());
        }
        return model;
    }

    private Job mapToJobModel(com.omniarch.talentvela.entity.Job entityJob) {
        Job modelJob = new Job();
        modelJob.setId(entityJob.getId());
        modelJob.setEmployerId(entityJob.getEmployerId());
        
        employerProfileRepository.findById(entityJob.getEmployerId()).ifPresent(emp -> {
            modelJob.setCompanyName(emp.getCompanyName());
            modelJob.setCompanyLogo(emp.getCompanyLogo());
        });
        
        modelJob.setTitle(entityJob.getTitle());
        modelJob.setDescription(entityJob.getDescription());
        modelJob.setRequiredSkills(entityJob.getRequiredSkills());
        modelJob.setExperienceRequired(entityJob.getExperienceRequired());
        if (entityJob.getSalaryMin() != null) modelJob.setSalaryMin(entityJob.getSalaryMin().doubleValue());
        if (entityJob.getSalaryMax() != null) modelJob.setSalaryMax(entityJob.getSalaryMax().doubleValue());
        modelJob.setJobType(entityJob.getJobType());
        modelJob.setWorkMode(entityJob.getWorkMode());
        modelJob.setOpenings(entityJob.getOpenings());
        if (entityJob.getApplicationDeadline() != null) modelJob.setApplicationDeadline(entityJob.getApplicationDeadline().toString());
        modelJob.setLocationAddress(entityJob.getLocationAddress());
        modelJob.setLocationCity(entityJob.getLocationCity());
        modelJob.setLocationState(entityJob.getLocationState());
        modelJob.setLocationCountry(entityJob.getLocationCountry());
        modelJob.setStatus(entityJob.getStatus());

        com.omniarch.talentvela.entity.JobSeekerProfile entityProfile = profileRepository.findByUserId(getCurrentUserId()).orElse(null);
        if (entityProfile != null && entityProfile.getSkills() != null && entityJob.getRequiredSkills() != null) {
            SkillMatcher.MatchResult res = SkillMatcher.matchSkills(entityProfile.getSkills(), entityJob.getRequiredSkills());
            modelJob.setMatchScore((int) Math.round(res.getScore()));
            modelJob.setMatchedSkills(new ArrayList<>(res.getMatchedSkills()));
            modelJob.setMissingSkills(new ArrayList<>(res.getMissingSkills()));
        }

        return modelJob;
    }

    private Application mapToAppModel(com.omniarch.talentvela.entity.Application entityApp) {
        Application modelApp = new Application();
        modelApp.setId(entityApp.getId());
        modelApp.setJobId(entityApp.getJobId());
        modelApp.setJobSeekerId(entityApp.getJobSeekerId());
        modelApp.setCoverLetterText(entityApp.getCoverLetterText());
        modelApp.setStatus(entityApp.getStatus());
        if (entityApp.getAppliedAt() != null) modelApp.setAppliedAt(entityApp.getAppliedAt().toString());
        
        jobRepository.findById(entityApp.getJobId()).ifPresent(job -> {
            modelApp.setJobTitle(job.getTitle());
            modelApp.setLocation(job.getLocationCity() + ", " + job.getLocationState());
            employerProfileRepository.findById(job.getEmployerId()).ifPresent(emp -> {
                modelApp.setCompanyName(emp.getCompanyName());
            });
        });
        
        profileRepository.findById(entityApp.getJobSeekerId()).ifPresent(prof -> {
            userRepository.findById(prof.getUserId()).ifPresent(user -> {
                modelApp.setCandidateName(user.getFullName());
            });
        });
        
        if (entityApp.getResumeId() != null) {
            resumeRepository.findById(entityApp.getResumeId()).ifPresent(res -> {
                modelApp.setResumeFileName(res.getFileName());
            });
        }
        
        return modelApp;
    }

    // ==========================================
    // 1. ROOT & DASHBOARD
    // ==========================================
    @GetMapping({"/", "/seeker", "/seeker/dashboard"})
    public String dashboard(Model model) {
        com.omniarch.talentvela.entity.JobSeekerProfile entityProfile = profileRepository.findByUserId(getCurrentUserId()).orElse(null);
        JobSeekerProfile currentProfile = mapToProfileModel(entityProfile);
        
        com.omniarch.talentvela.entity.Resume entityResume = entityProfile != null ? 
            resumeRepository.findByJobSeekerId(entityProfile.getId()).orElse(null) : null;
        Resume currentResume = mapToResumeModel(entityResume);
        
        List<com.omniarch.talentvela.entity.Application> entityApps = 
            entityProfile != null ? applicationRepository.findByJobSeekerId(entityProfile.getId()) : new ArrayList<>();
            
        long totalApplied = entityApps.size();
        long underReview = entityApps.stream().filter(a -> "UNDER_REVIEW".equalsIgnoreCase(a.getStatus())).count();
        long shortlisted = entityApps.stream().filter(a -> "SHORTLISTED".equalsIgnoreCase(a.getStatus())).count();
        long selected = entityApps.stream().filter(a -> "SELECTED".equalsIgnoreCase(a.getStatus())).count();

        List<Application> recentApplications = entityApps.stream()
                .map(this::mapToAppModel)
                .collect(Collectors.toList());

        List<Job> recommendedJobs = jobRepository.findByStatus("PUBLISHED").stream()
                .map(this::mapToJobModel)
                .limit(3)
                .collect(Collectors.toList());

        model.addAttribute("profile", currentProfile);
        model.addAttribute("resume", currentResume);
        model.addAttribute("totalApplied", totalApplied);
        model.addAttribute("underReview", underReview);
        model.addAttribute("shortlisted", shortlisted);
        model.addAttribute("selected", selected);
        model.addAttribute("recentApplications", recentApplications);
        model.addAttribute("recommendedJobs", recommendedJobs);
        return "jobseeker/dashboard";
    }

    // ==========================================
    // 2. JOB SEARCH & LISTING
    // ==========================================
    @GetMapping("/seeker/jobs")
    public String listJobs(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "location", required = false) String location,
            @RequestParam(value = "workMode", required = false) String workMode,
            @RequestParam(value = "jobType", required = false) String jobType,
            Model model) {

        List<Job> filtered = jobRepository.findByStatus("PUBLISHED").stream()
                .map(this::mapToJobModel)
                .filter(j -> {
                    boolean match = true;
                    if (keyword != null && !keyword.isBlank()) {
                        String kw = keyword.toLowerCase().trim();
                        boolean titleMatch = j.getTitle() != null && j.getTitle().toLowerCase().contains(kw);
                        boolean skillMatch = j.getRequiredSkills() != null && j.getRequiredSkills().toLowerCase().contains(kw);
                        boolean compMatch = j.getCompanyName() != null && j.getCompanyName().toLowerCase().contains(kw);
                        match = titleMatch || skillMatch || compMatch;
                    }
                    if (match && location != null && !location.isBlank()) {
                        String loc = location.toLowerCase().trim();
                        match = (j.getLocationCity() != null && j.getLocationCity().toLowerCase().contains(loc))
                                || (j.getLocationState() != null && j.getLocationState().toLowerCase().contains(loc));
                    }
                    if (match && workMode != null && !workMode.isBlank() && !"ALL".equalsIgnoreCase(workMode)) {
                        match = workMode.equalsIgnoreCase(j.getWorkMode());
                    }
                    if (match && jobType != null && !jobType.isBlank() && !"ALL".equalsIgnoreCase(jobType)) {
                        match = jobType.equalsIgnoreCase(j.getJobType());
                    }
                    return match;
                }).collect(Collectors.toList());

        com.omniarch.talentvela.entity.JobSeekerProfile entityProfile = profileRepository.findByUserId(getCurrentUserId()).orElse(null);
        Set<Integer> appliedJobIds = entityProfile != null ? 
            applicationRepository.findByJobSeekerId(entityProfile.getId()).stream()
                .map(com.omniarch.talentvela.entity.Application::getJobId)
                .collect(Collectors.toSet()) : new HashSet<>();

        model.addAttribute("jobs", filtered);
        model.addAttribute("appliedJobIds", appliedJobIds);
        model.addAttribute("keyword", keyword);
        model.addAttribute("location", location);
        model.addAttribute("workMode", workMode != null ? workMode : "ALL");
        model.addAttribute("jobType", jobType != null ? jobType : "ALL");
        model.addAttribute("totalJobsFound", filtered.size());
        return "jobseeker/jobs";
    }

    // ==========================================
    // 3. JOB DETAILS
    // ==========================================
    @GetMapping("/seeker/jobs/{id}")
    public String jobDetail(@PathVariable("id") Integer id, Model model) {
        com.omniarch.talentvela.entity.Job entityJob = jobRepository.findById(id).orElse(null);
        if (entityJob == null) {
            return "redirect:/seeker/jobs";
        }
        Job job = mapToJobModel(entityJob);

        com.omniarch.talentvela.entity.JobSeekerProfile entityProfile = profileRepository.findByUserId(getCurrentUserId()).orElse(null);
        com.omniarch.talentvela.entity.Resume entityResume = entityProfile != null ? 
            resumeRepository.findByJobSeekerId(entityProfile.getId()).orElse(null) : null;
        
        Resume currentResume = mapToResumeModel(entityResume);
        boolean alreadyApplied = entityProfile != null && applicationRepository.existsByJobIdAndJobSeekerId(id, entityProfile.getId());
        
        model.addAttribute("job", job);
        model.addAttribute("alreadyApplied", alreadyApplied);
        model.addAttribute("resume", currentResume);
        return "jobseeker/job-detail";
    }

    // ==========================================
    // 4. APPLY PAGE (GET & POST)
    // ==========================================
    @GetMapping("/seeker/jobs/{id}/apply")
    public String applyForm(@PathVariable("id") Integer id, Model model, RedirectAttributes ra) {
        com.omniarch.talentvela.entity.Job entityJob = jobRepository.findById(id).orElse(null);
        if (entityJob == null) {
            ra.addFlashAttribute("errorMessage", "Job position not found.");
            return "redirect:/seeker/jobs";
        }
        Job job = mapToJobModel(entityJob);

        com.omniarch.talentvela.entity.JobSeekerProfile entityProfile = profileRepository.findByUserId(getCurrentUserId()).orElse(null);
        com.omniarch.talentvela.entity.Resume entityResume = entityProfile != null ? 
            resumeRepository.findByJobSeekerId(entityProfile.getId()).orElse(null) : null;

        JobSeekerProfile currentProfile = mapToProfileModel(entityProfile);
        Resume currentResume = mapToResumeModel(entityResume);

        if (job.isDeadlinePassed()) {
            ra.addFlashAttribute("errorMessage", "Applications for this position are closed because the deadline has passed.");
            return "redirect:/seeker/jobs/" + id;
        }

        if (currentResume == null || currentResume.getFileName() == null || currentResume.getFileName().isBlank()) {
            ra.addFlashAttribute("errorMessage", "Resume required: You must upload a resume before applying for jobs.");
            return "redirect:/seeker/resume";
        }

        boolean alreadyApplied = entityProfile != null && applicationRepository.existsByJobIdAndJobSeekerId(id, entityProfile.getId());
        if (alreadyApplied) {
            ra.addFlashAttribute("errorMessage", "You have already applied for this position: " + job.getTitle());
            return "redirect:/seeker/jobs/" + id;
        }

        model.addAttribute("job", job);
        model.addAttribute("profile", currentProfile);
        model.addAttribute("resume", currentResume);
        return "jobseeker/apply";
    }

    @PostMapping("/seeker/jobs/{id}/apply")
    public String submitApplication(
            @PathVariable("id") Integer id,
            @RequestParam(value = "coverLetter", required = false) String coverLetter,
            RedirectAttributes ra) {

        com.omniarch.talentvela.entity.Job entityJob = jobRepository.findById(id).orElse(null);
        if (entityJob == null) {
            ra.addFlashAttribute("errorMessage", "Job not found.");
            return "redirect:/seeker/jobs";
        }
        
        com.omniarch.talentvela.entity.JobSeekerProfile entityProfile = profileRepository.findByUserId(getCurrentUserId()).orElse(null);
        com.omniarch.talentvela.entity.Resume entityResume = entityProfile != null ? 
            resumeRepository.findByJobSeekerId(entityProfile.getId()).orElse(null) : null;

        if (entityProfile != null && applicationRepository.existsByJobIdAndJobSeekerId(id, entityProfile.getId())) {
            ra.addFlashAttribute("errorMessage", "Duplicate application prevented. You already applied for this job.");
            return "redirect:/seeker/applications";
        }

        com.omniarch.talentvela.entity.Application newApp = new com.omniarch.talentvela.entity.Application();
        newApp.setJobId(entityJob.getId());
        if (entityProfile != null) newApp.setJobSeekerId(entityProfile.getId());
        newApp.setResumeId(entityResume != null ? entityResume.getId() : null);
        newApp.setCoverLetterText(coverLetter != null && !coverLetter.isBlank() ? coverLetter : "Candidate applied with standard profile.");
        newApp.setStatus("APPLIED");

        applicationRepository.save(newApp);

        Job jobModel = mapToJobModel(entityJob);
        ra.addFlashAttribute("successMessage", "🎉 Application submitted successfully for " + jobModel.getTitle() + " at " + jobModel.getCompanyName() + "!");
        return "redirect:/seeker/applications";
    }

    // ==========================================
    // 5. MY APPLICATIONS
    // ==========================================
    @GetMapping("/seeker/applications")
    public String myApplications(
            @RequestParam(value = "statusFilter", required = false) String statusFilter,
            Model model) {

        com.omniarch.talentvela.entity.JobSeekerProfile entityProfile = profileRepository.findByUserId(getCurrentUserId()).orElse(null);
        List<com.omniarch.talentvela.entity.Application> entityApps = entityProfile != null ? 
            applicationRepository.findByJobSeekerId(entityProfile.getId()) : new ArrayList<>();

        List<Application> filtered = entityApps.stream()
                .filter(a -> statusFilter == null || statusFilter.isBlank() || "ALL".equalsIgnoreCase(statusFilter) || statusFilter.equalsIgnoreCase(a.getStatus()))
                .map(this::mapToAppModel)
                .collect(Collectors.toList());

        model.addAttribute("applications", filtered);
        model.addAttribute("statusFilter", statusFilter != null ? statusFilter : "ALL");
        model.addAttribute("totalCount", entityApps.size());
        return "jobseeker/applications";
    }

    @GetMapping("/seeker/applications/{id}")
    public String applicationDetails(@PathVariable("id") Integer id, Model model) {
        com.omniarch.talentvela.entity.Application entityApp = applicationRepository.findById(id).orElse(null);
        if (entityApp == null) {
            return "redirect:/seeker/applications";
        }

        model.addAttribute("app", mapToAppModel(entityApp));
        return "jobseeker/application-details";
    }

    @PostMapping("/seeker/applications/{id}/withdraw")
    public String withdrawApplication(@PathVariable("id") Integer id, RedirectAttributes ra) {
        if (applicationRepository.existsById(id)) {
            applicationRepository.deleteById(id);
            ra.addFlashAttribute("successMessage", "Application #" + id + " has been withdrawn.");
        } else {
            ra.addFlashAttribute("errorMessage", "Application not found.");
        }
        return "redirect:/seeker/applications";
    }

    // ==========================================
    // 6. PROFILE MANAGEMENT
    // ==========================================
    @GetMapping("/seeker/profile")
    public String viewProfile(Model model) {
        com.omniarch.talentvela.entity.JobSeekerProfile entityProfile = profileRepository.findByUserId(getCurrentUserId()).orElse(null);
        com.omniarch.talentvela.entity.Resume entityResume = entityProfile != null ? 
            resumeRepository.findByJobSeekerId(entityProfile.getId()).orElse(null) : null;
            
        model.addAttribute("profile", mapToProfileModel(entityProfile));
        model.addAttribute("resume", mapToResumeModel(entityResume));
        return "jobseeker/profile";
    }

    @GetMapping("/seeker/profile/edit")
    public String editProfile(Model model) {
        com.omniarch.talentvela.entity.JobSeekerProfile entityProfile = profileRepository.findByUserId(getCurrentUserId()).orElse(null);
        model.addAttribute("profile", mapToProfileModel(entityProfile));
        return "jobseeker/edit-profile";
    }

    @PostMapping("/seeker/profile")
    public String updateProfile(@ModelAttribute JobSeekerProfile form, RedirectAttributes ra) {
        com.omniarch.talentvela.entity.JobSeekerProfile entityProfile = profileRepository.findByUserId(getCurrentUserId()).orElse(null);
        if (entityProfile != null) {
            // Update User for name and phone
            userRepository.findById(entityProfile.getUserId()).ifPresent(user -> {
                user.setFullName(form.getFullName());
                user.setPhone(form.getPhone());
                userRepository.save(user);
            });
            
            entityProfile.setHeadline(form.getHeadline());
            entityProfile.setBio(form.getBio());
            entityProfile.setCity(form.getCity());
            entityProfile.setState(form.getState());
            entityProfile.setCountry(form.getCountry());
            entityProfile.setDesiredRole(form.getDesiredRole());
            entityProfile.setExperienceYears(form.getExperienceYears());
            entityProfile.setSkills(form.getSkills());
            entityProfile.setHighestQualification(form.getHighestQualification());
            entityProfile.setCollegeUniversity(form.getCollegeUniversity());
            entityProfile.setFieldOfStudy(form.getFieldOfStudy());
            entityProfile.setGraduationYear(form.getGraduationYear());
            entityProfile.setPreferredLocation(form.getPreferredLocation());
            entityProfile.setPreferredWorkMode(form.getPreferredWorkMode());
            entityProfile.setExpectedSalary(form.getExpectedSalary());
            entityProfile.setNoticePeriod(form.getNoticePeriod());
            
            profileRepository.save(entityProfile);
            ra.addFlashAttribute("successMessage", "✅ Profile updated successfully!");
        }
        return "redirect:/seeker/profile";
    }

    // ==========================================
    // 7. RESUME & SKILL INTELLIGENCE
    // ==========================================
    @GetMapping("/seeker/resume")
    public String viewResume(Model model) {
        com.omniarch.talentvela.entity.JobSeekerProfile entityProfile = profileRepository.findByUserId(getCurrentUserId()).orElse(null);
        com.omniarch.talentvela.entity.Resume entityResume = entityProfile != null ? 
            resumeRepository.findByJobSeekerId(entityProfile.getId()).orElse(null) : null;
            
        JobSeekerProfile currentProfile = mapToProfileModel(entityProfile);
        Resume currentResume = mapToResumeModel(entityResume);
        
        List<String> targetJobSkills = List.of("Java", "Spring Boot", "MySQL", "REST API", "Docker", "AWS", "Git");
        List<String> candidateSkills = currentResume != null && currentResume.getExtractedSkills() != null ? 
            Arrays.asList(currentResume.getExtractedSkills().split(",")) : new ArrayList<>();

        List<String> matched = targetJobSkills.stream()
                .filter(s -> candidateSkills.stream().anyMatch(cs -> cs.trim().equalsIgnoreCase(s)))
                .collect(Collectors.toList());

        List<String> missing = targetJobSkills.stream()
                .filter(s -> candidateSkills.stream().noneMatch(cs -> cs.trim().equalsIgnoreCase(s)))
                .collect(Collectors.toList());

        int readinessScore = targetJobSkills.isEmpty() ? 0 : (matched.size() * 100) / targetJobSkills.size();

        model.addAttribute("resume", currentResume);
        model.addAttribute("profile", currentProfile);
        model.addAttribute("targetJobSkills", targetJobSkills);
        model.addAttribute("matchedSkills", matched);
        model.addAttribute("missingSkills", missing);
        model.addAttribute("readinessScore", readinessScore);
        return "jobseeker/resume";
    }

    @PostMapping("/seeker/resume/upload")
    public String uploadResume(
            @RequestParam("resumeFile") MultipartFile file,
            @RequestParam(value = "newSkills", required = false) String newSkills,
            RedirectAttributes ra) {

        if (file.isEmpty()) {
            ra.addFlashAttribute("errorMessage", "Please select a valid PDF/Word resume file to upload.");
            return "redirect:/seeker/resume";
        }

        com.omniarch.talentvela.entity.JobSeekerProfile entityProfile = profileRepository.findByUserId(getCurrentUserId()).orElse(null);
        com.omniarch.talentvela.entity.Resume entityResume = entityProfile != null ? 
            resumeRepository.findByJobSeekerId(entityProfile.getId()).orElse(null) : null;
        
        if (entityResume == null && entityProfile != null) {
            entityResume = new com.omniarch.talentvela.entity.Resume();
            entityResume.setJobSeekerId(entityProfile.getId());
        }

        if (entityResume != null) {
            String originalName = file.getOriginalFilename();
            entityResume.setFileName(originalName);

            if (newSkills != null && !newSkills.isBlank()) {
                entityResume.setExtractedSkills(newSkills);
                if (entityProfile != null) {
                    entityProfile.setSkills(newSkills);
                    profileRepository.save(entityProfile);
                }
            }
            resumeRepository.save(entityResume);
            ra.addFlashAttribute("successMessage", "📄 Resume '" + originalName + "' uploaded and skills extracted successfully!");
        }

        return "redirect:/seeker/resume";
    }
}
