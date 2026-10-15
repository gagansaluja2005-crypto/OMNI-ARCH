package com.omniarch.talentvela.service.matching;

import org.springframework.stereotype.Service;
import org.springframework.stereotype.Component;

import java.util.*;
@Service
public class RankingService {
    public static class Candidate {
        private final String name;
        private final String skills;
        public Candidate(String name, String skills) {
            this.name = name;
            this.skills = skills;
        }
        public String getName() {
            return name;
        }
        public String getSkills() {
            return skills;
        }
    }
    public static class Job {
        private final String title;
        private final String requiredSkills;
        public Job(String title, String requiredSkills) {
            this.title = title;
            this.requiredSkills = requiredSkills;
        }
        public String getTitle() {
            return title;
        }
        public String getRequiredSkills() {
            return requiredSkills;
        }
    }
    public static class CandidateScore {
        private final Candidate candidate;
        private final double score;
        public CandidateScore(Candidate candidate, double score) {
            this.candidate = candidate;
            this.score = score;
        }
        public Candidate getCandidate() {
            return candidate;
        }
        public double getScore() {
            return score;
        }
    }
    public static class JobScore {
        private final Job job;
        private final double score;
        public JobScore(Job job, double score) {
            this.job = job;
            this.score = score;
        }
        public Job getJob() {
            return job;
        }
        public double getScore() {
            return score;
        }
    }
    public static List<CandidateScore> rankCandidatesForJob(List<Candidate> candidates, Job job) {
        List<CandidateScore> results = new ArrayList<>();

        for (Candidate candidate : candidates) {
            SkillMatcher.MatchResult result = SkillMatcher.matchSkills(
                    candidate.getSkills(),
                    job.getRequiredSkills()
            );
            results.add(new CandidateScore(candidate, result.getScore()));
        }
        results.sort(Comparator.comparingDouble(CandidateScore::getScore).reversed());
        return results;
    }
    /**
     * Task 7 integration helper for real project entities. The caller supplies
     * candidate skills keyed by Application.jobSeekerId; this keeps the matching
     * engine independent from repository/UI code.
     */
    public List<com.omniarch.talentvela.entity.Application> rankApplicationsForJob(
            com.omniarch.talentvela.entity.Job job,
            List<com.omniarch.talentvela.entity.Application> applications,
            java.util.Map<Integer, String> candidateSkillsBySeekerId) {

        if (job == null || applications == null || applications.isEmpty()) {
            return new ArrayList<>();
        }

        List<com.omniarch.talentvela.entity.Application> ranked = new ArrayList<>(applications);
        ranked.sort((a, b) -> {
            String skillsA = candidateSkillsBySeekerId == null ? ""
                    : candidateSkillsBySeekerId.getOrDefault(a.getJobSeekerId(), "");
            String skillsB = candidateSkillsBySeekerId == null ? ""
                    : candidateSkillsBySeekerId.getOrDefault(b.getJobSeekerId(), "");
            double scoreA = SkillMatcher.matchSkills(skillsA, job.getRequiredSkills()).getScore();
            double scoreB = SkillMatcher.matchSkills(skillsB, job.getRequiredSkills()).getScore();
            return Double.compare(scoreB, scoreA);
        });
        return ranked;
    }

    public static List<JobScore> rankJobsForCandidate(Candidate candidate, List<Job> jobs) {
        List<JobScore> results = new ArrayList<>();

        for (Job job : jobs) {
            SkillMatcher.MatchResult result = SkillMatcher.matchSkills(
                    candidate.getSkills(),
                    job.getRequiredSkills()
            );

            results.add(new JobScore(job, result.getScore()));
        }
        results.sort(Comparator.comparingDouble(JobScore::getScore).reversed());
        return results;
    }
    public static void main(String[] args) {
        List<Candidate> candidates = Arrays.asList(
                new Candidate("Rahul", "Java, Spring, SQL, AWS"),
                new Candidate("Aman", "Java, Spring, SQL"),
                new Candidate("Rohit", "Java, SQL"),
                new Candidate("Karan", "Python, HTML")
        );
        Job javaJob = new Job("Java Developer", "Java, Spring, SQL, AWS");
        System.out.println("CANDIDATE RANKING FOR JAVA DEVELOPER");
        System.out.println("-----------------------------------");
        List<CandidateScore> candidateRanking = rankCandidatesForJob(candidates, javaJob);
        int rank = 1;

        for (CandidateScore result : candidateRanking) {
            System.out.printf(
                    "%d. %s - %.2f%%%n",
                    rank++,
                    result.getCandidate().getName(),
                    result.getScore()
            );
        }
        Candidate candidate = candidates.get(0);

        List<Job> jobs = Arrays.asList(
                new Job("Java Developer", "Java, Spring, SQL, AWS"),
                new Job("Backend Developer", "Java, SQL"),
                new Job("Python Developer", "Python, Django, SQL"),
                new Job("Frontend Developer", "HTML, CSS, JavaScript")
        );
        System.out.println("\nJOB RANKING FOR " + candidate.getName().toUpperCase());
        System.out.println("-----------------------------------");
        List<JobScore> jobRanking = rankJobsForCandidate(candidate, jobs);
        rank = 1;
        for (JobScore result : jobRanking) {
            System.out.printf(
                    "%d. %s - %.2f%%%n",
                    rank++,
                    result.getJob().getTitle(),
                    result.getScore()
            );
        }
    }
}