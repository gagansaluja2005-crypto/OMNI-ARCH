package com.omniarch.talentvela.service.matching;

public class MatchingTest {
    public static void main(String[] args) {
        test("Scenario 1 - Exact Match", "Java, Spring, SQL", "Java, Spring, SQL", 100.0);
        test("Scenario 2 - Partial Match", "Java, SQL", "Java, Spring, SQL", 66.67);
        test("Scenario 3 - No Match", "Python, HTML", "Java, Spring, SQL", 0.0);
        test("Scenario 4 - Alias Match", "Java, Spring Boot, MySQL", "Java, Spring, SQL", 100.0);
        test("Scenario 5 - Empty Skills", "", "Java, Spring, SQL", 0.0);
    }
    private static void test(String name, String candidateSkills, String jobSkills, double expected) {
        SkillMatcher.MatchResult result = SkillMatcher.matchSkills(candidateSkills, jobSkills);
        double actual = result.getScore();
        System.out.println(name);
        System.out.printf("Expected Score: %.2f%%%n", expected);
        System.out.printf("Actual Score: %.2f%%%n", actual);
        System.out.println("Status: " + (Math.abs(actual - expected) < 0.01 ? "PASS" : "FAIL"));
        System.out.println();
    }
}