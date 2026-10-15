package com.omniarch.talentvela.service.matching;

import org.springframework.stereotype.Service;
import org.springframework.stereotype.Component;

@Service
public class SkillGapService {
    public static SkillMatcher.MatchResult calculateSkillGap(
            String candidateSkills,
            String requiredSkills) {
        return SkillMatcher.matchSkills(candidateSkills, requiredSkills);
    }
    public static void main(String[] args) {

        String candidateSkills = "Java, Spring, SQL";
        String requiredSkills = "Java, Spring, SQL, AWS";

        SkillMatcher.MatchResult result =
                calculateSkillGap(candidateSkills, requiredSkills);

        System.out.println("Candidate Skills:");
        System.out.println(candidateSkills);

        System.out.println("\nRequired Job Skills:");
        System.out.println(requiredSkills);

        System.out.println("\n========== SKILL GAP RESULT ==========");
        System.out.println("Matched Skills: " + result.getMatchedSkills());
        System.out.println("Partial Skills: " + result.getPartialSkills());
        System.out.println("Missing Skills: " + result.getMissingSkills());
        System.out.println("Match Score: " +
                String.format("%.2f", result.getScore()) + "%");
    }
}