package com.omniarch.talentvela.service.matching;

import org.springframework.stereotype.Service;
import org.springframework.stereotype.Component;

import java.util.*;

@Service
public class RecommendationService {

    public static List<RankingService.JobScore> recommendJobsForCandidate(
            RankingService.Candidate candidate,
            List<RankingService.Job> jobs) {

        List<RankingService.JobScore> recommendations =
                RankingService.rankJobsForCandidate(candidate, jobs);

        return recommendations;
    }

    public static List<RankingService.CandidateScore> recommendCandidatesForJob(
            List<RankingService.Candidate> candidates,
            RankingService.Job job) {

        List<RankingService.CandidateScore> recommendations =
                RankingService.rankCandidatesForJob(candidates, job);

        return recommendations;
    }

    public static List<RankingService.JobScore> recommendTopJobsForCandidate(
            RankingService.Candidate candidate,
            List<RankingService.Job> jobs,
            int limit) {

        List<RankingService.JobScore> recommendations =
                RankingService.rankJobsForCandidate(candidate, jobs);

        if (limit <= 0) {
            return new ArrayList<>();
        }

        return new ArrayList<>(
                recommendations.subList(
                        0,
                        Math.min(limit, recommendations.size())
                )
        );
    }

    public static List<RankingService.CandidateScore> recommendTopCandidatesForJob(
            List<RankingService.Candidate> candidates,
            RankingService.Job job,
            int limit) {

        List<RankingService.CandidateScore> recommendations =
                RankingService.rankCandidatesForJob(candidates, job);

        if (limit <= 0) {
            return new ArrayList<>();
        }

        return new ArrayList<>(
                recommendations.subList(
                        0,
                        Math.min(limit, recommendations.size())
                )
        );
    }

    public static void main(String[] args) {

        List<RankingService.Candidate> candidates = Arrays.asList(
                new RankingService.Candidate(
                        "Rahul",
                        "Java, Spring, SQL, AWS"
                ),
                new RankingService.Candidate(
                        "Aman",
                        "Java, Spring, SQL"
                ),
                new RankingService.Candidate(
                        "Rohit",
                        "Java, SQL"
                ),
                new RankingService.Candidate(
                        "Karan",
                        "Python, HTML"
                )
        );

        RankingService.Job javaJob =
                new RankingService.Job(
                        "Java Developer",
                        "Java, Spring, SQL, AWS"
                );

        System.out.println("TOP CANDIDATES FOR JAVA DEVELOPER");
        System.out.println("--------------------------------");

        List<RankingService.CandidateScore> topCandidates =
                recommendTopCandidatesForJob(
                        candidates,
                        javaJob,
                        3
                );

        int rank = 1;

        for (RankingService.CandidateScore result : topCandidates) {
            System.out.printf(
                    "%d. %s - %.2f%%%n",
                    rank++,
                    result.getCandidate().getName(),
                    result.getScore()
            );
        }

        RankingService.Candidate candidate = candidates.get(0);

        List<RankingService.Job> jobs = Arrays.asList(
                new RankingService.Job(
                        "Java Developer",
                        "Java, Spring, SQL, AWS"
                ),
                new RankingService.Job(
                        "Backend Developer",
                        "Java, SQL"
                ),
                new RankingService.Job(
                        "Python Developer",
                        "Python, Django, SQL"
                ),
                new RankingService.Job(
                        "Frontend Developer",
                        "HTML, CSS, JavaScript"
                )
        );

        System.out.println(
                "\nTOP JOB RECOMMENDATIONS FOR "
                        + candidate.getName().toUpperCase()
        );
        System.out.println("--------------------------------");

        List<RankingService.JobScore> topJobs =
                recommendTopJobsForCandidate(
                        candidate,
                        jobs,
                        3
                );

        rank = 1;

        for (RankingService.JobScore result : topJobs) {
            System.out.printf(
                    "%d. %s - %.2f%%%n",
                    rank++,
                    result.getJob().getTitle(),
                    result.getScore()
            );
        }
    }
}