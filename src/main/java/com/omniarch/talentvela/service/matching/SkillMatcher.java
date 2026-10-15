package com.omniarch.talentvela.service.matching;

import org.springframework.stereotype.Service;
import org.springframework.stereotype.Component;

import java.util.*;
@Service
public class SkillMatcher {
    public static class MatchResult {
        private final Set<String> matchedSkills;
        private final Set<String> partialSkills;
        private final Set<String> missingSkills;
        private final double score;
        public MatchResult(Set<String> matchedSkills, Set<String> partialSkills,
                           Set<String> missingSkills, double score) {
            this.matchedSkills = matchedSkills;
            this.partialSkills = partialSkills;
            this.missingSkills = missingSkills;
            this.score = score;
        }
        public Set<String> getMatchedSkills() {
            return matchedSkills;
        }
        public Set<String> getPartialSkills() {
            return partialSkills;
        }
        public Set<String> getMissingSkills() {
            return missingSkills;
        }
        public double getScore() {
            return score;
        }

        public int getMatchPercentage() {
            return (int) Math.round(score);
        }
        @Override
        public String toString() {
            return "Matched Skills: " + matchedSkills +
                   "\nPartial Skills: " + partialSkills +
                   "\nMissing Skills: " + missingSkills +
                   "\nScore: " + String.format("%.2f", score) + "%";
        }
    }

    public static MatchResult matchSkills(String candidateSkills, String requiredSkills) {
        Set<String> candidate = SkillNormalizer.normalizeSkills(candidateSkills);
        Set<String> required = SkillNormalizer.normalizeSkills(requiredSkills);
        Set<String> matched = new LinkedHashSet<>();
        Set<String> partial = new LinkedHashSet<>();
        Set<String> missing = new LinkedHashSet<>();
        if (required.isEmpty()) {
            return new MatchResult(matched, partial, missing, 0.0);
        }
        for (String requiredSkill : required) {
            if (candidate.contains(requiredSkill)) {
                matched.add(requiredSkill);
            } else if (hasPartialMatch(candidate, requiredSkill)) {
                partial.add(requiredSkill);
            } else {
                missing.add(requiredSkill);
            }
        }
        double score = ((double) matched.size() / required.size()) * 100.0;

        return new MatchResult(matched, partial, missing, score);
    }
    /**
     * Task 6 helper overload for Spring services/controllers.
     */
    public static MatchResult matchSkills(List<String> candidateSkills,
                                          List<String> requiredSkills) {
        String candidate = candidateSkills == null ? "" : String.join(",", candidateSkills);
        String required = requiredSkills == null ? "" : String.join(",", requiredSkills);
        return matchSkills(candidate, required);
    }

    private static boolean hasPartialMatch(Set<String> candidateSkills, String requiredSkill) {
        for (String candidateSkill : candidateSkills) {
            if (isPartialMatch(candidateSkill, requiredSkill)) {
                return true;
            }
        }
        return false;
    }
    private static boolean isPartialMatch(String candidateSkill, String requiredSkill) {
        if (candidateSkill.equals(requiredSkill)) {
            return false;
        }
        Set<String> candidateWords = new HashSet<>(Arrays.asList(candidateSkill.split("\\s+")));
        Set<String> requiredWords = new HashSet<>(Arrays.asList(requiredSkill.split("\\s+")));
        candidateWords.retainAll(requiredWords);
        return !candidateWords.isEmpty();
    }
    public static void main(String[] args) {
        String candidateSkills = "Java, Spring, SQL, HTML";
        String requiredSkills = "Java, Spring, SQL, AWS";
        System.out.println("Candidate Skills:");
        System.out.println(candidateSkills);
        System.out.println("\nRequired Job Skills:");
        System.out.println(requiredSkills);
        MatchResult result = matchSkills(candidateSkills, requiredSkills);
        System.out.println("\n========== MATCH RESULT ==========");
        System.out.println(result);
    }
}