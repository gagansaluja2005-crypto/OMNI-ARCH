package com.omniarch.talentvela.controller;

import com.omniarch.talentvela.entity.Application;
import com.omniarch.talentvela.entity.EmployerProfile;
import com.omniarch.talentvela.entity.Job;
import com.omniarch.talentvela.repository.ApplicationRepository;
import com.omniarch.talentvela.repository.EmployerProfileRepository;
import com.omniarch.talentvela.repository.JobRepository;

// Depending on Chitransh and Gagan's packages, these imports map to their work
import com.omniarch.talentvela.service.matching.RankingService;
import com.omniarch.talentvela.security.SecurityUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/employer")
public class EmployerController {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private EmployerProfileRepository employerProfileRepository;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private RankingService rankingService; // Chitransh's AI Engine Service

    // GET /employer/dashboard
    @GetMapping("/dashboard")
    public String getDashboard(Model model) {
        Integer userId = SecurityUtil.getCurrentUserIdOrFallback(1); // Gagan's Auth Utility
        Optional<EmployerProfile> profileOpt = employerProfileRepository.findByUserId(userId);
        
        if (profileOpt.isPresent()) {
            model.addAttribute("profile", profileOpt.get());
            // Fetch jobs to show on dashboard
            List<Job> jobs = jobRepository.findByEmployerId(profileOpt.get().getId());
            model.addAttribute("jobs", jobs);
        }
        
        return "employer/dashboard";
    }

    // GET /employer/profile
    @GetMapping("/profile")
    public String getProfileForm(Model model) {
        Integer userId = SecurityUtil.getCurrentUserIdOrFallback(1);
        EmployerProfile profile = employerProfileRepository.findByUserId(userId)
                .orElse(new EmployerProfile());
        model.addAttribute("profile", profile);
        return "employer/profile";
    }

    // POST /employer/profile
    @PostMapping("/profile")
    public String saveProfile(@ModelAttribute EmployerProfile profile) {
        Integer userId = SecurityUtil.getCurrentUserIdOrFallback(1);
        
        // Check if profile exists to update vs insert
        Optional<EmployerProfile> existingProfile = employerProfileRepository.findByUserId(userId);
        if (existingProfile.isPresent()) {
            profile.setId(existingProfile.get().getId());
        }
        profile.setUserId(userId);
        employerProfileRepository.save(profile);
        
        return "redirect:/employer/dashboard";
    }

    // GET /employer/jobs/create
    @GetMapping("/jobs/create")
    public String getCreateJobForm(Model model) {
        model.addAttribute("job", new Job());
        return "employer/create-job";
    }

    // POST /employer/jobs/create
    @PostMapping("/jobs/create")
    public String createJob(@ModelAttribute Job job) {
        Integer userId = SecurityUtil.getCurrentUserIdOrFallback(1);
        EmployerProfile profile = employerProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Employer Profile not found. Please complete profile first."));
        
        job.setEmployerId(profile.getId());
        // Defaulting to PUBLISHED so seekers can see it immediately, as per Admin flow note
        job.setStatus("PUBLISHED"); 
        jobRepository.save(job);
        
        return "redirect:/employer/dashboard";
    }

    // GET /employer/jobs/{id}/applicants
    @GetMapping("/jobs/{id}/applicants")
    public String getApplicantsForJob(@PathVariable("id") Integer jobId, Model model) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));
                
        // Fetch all applications for this job
        List<Application> applications = applicationRepository.findByJobId(jobId);
        
        // Pass to Chitransh's RankingService to sort from highest to lowest match
        List<Application> rankedApplications = rankingService.rankApplicationsForJob(job, applications, null);
        
        model.addAttribute("job", job);
        model.addAttribute("applications", rankedApplications);
        
        return "employer/ranked-applicants";
    }

    // POST /employer/applications/{id}/status
    @PostMapping("/applications/{id}/status")
    public String updateApplicationStatus(@PathVariable("id") Integer applicationId, 
                                          @RequestParam("status") String status) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));
                
        // Update status (e.g., SHORTLISTED, REJECTED)
        application.setStatus(status);
        applicationRepository.save(application);
        
        // Redirect back to the applicants page for the specific job
        return "redirect:/employer/jobs/" + application.getJobId() + "/applicants";
    }
}
