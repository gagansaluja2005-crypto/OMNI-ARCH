package com.omniarch.talentvela.service.matching;

import org.springframework.stereotype.Service;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.regex.Pattern;
@Service
public class SkillExtractor {
    private static final List<String> SKILL_DICTIONARY = Arrays.asList(
        "spring boot",
        "javascript",
        "react.js",
        "reactjs",
        "node.js",
        "python",
        "mysql",
        "java",
        "react",
        "spring",
        "sql",
        "html",
        "css",
        "c++",
        "c"
    );
    public static Set<String> extractSkills(String text) {
        Set<String> extractedSkills = new LinkedHashSet<>();
        if (text == null || text.trim().isEmpty()) {
            return extractedSkills;
        }
        String lowerText = text.toLowerCase();
        for (String skill : SKILL_DICTIONARY) {
            String regex = "(?<![a-z0-9])"
                    + Pattern.quote(skill)
                    + "(?![a-z0-9])";
            if (Pattern.compile(regex).matcher(lowerText).find()) {
                extractedSkills.add(skill);
            }
        }
        String skillsForNormalization = String.join(", ", extractedSkills);
        return SkillNormalizer.normalizeSkills(skillsForNormalization);
    }
    public static void main(String[] args) {
        String resumeText =
                "I am a Java developer with Spring Boot, MySQL, "
                + "JavaScript and React.js experience.";
        Set<String> result = extractSkills(resumeText);
        System.out.println("Resume Text:");
        System.out.println(resumeText);
        System.out.println("\nExtracted Skills:");
        System.out.println(result);
    }
}