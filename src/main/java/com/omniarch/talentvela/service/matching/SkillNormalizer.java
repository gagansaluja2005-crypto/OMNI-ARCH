package com.omniarch.talentvela.service.matching;

import org.springframework.stereotype.Service;
import org.springframework.stereotype.Component;

import java.util.*;
@Service
public class SkillNormalizer {
    private static final Map<String, String> ALIASES = new HashMap<>();
    static {
        ALIASES.put("js", "javascript");
        ALIASES.put("javascript", "javascript");
        ALIASES.put("react.js", "react");
        ALIASES.put("reactjs", "react");
        ALIASES.put("react", "react");
        ALIASES.put("spring boot", "spring");
        ALIASES.put("springboot", "spring");
        ALIASES.put("spring", "spring");
        ALIASES.put("sql", "sql");
        ALIASES.put("mysql", "sql");
        ALIASES.put("python", "python");
        ALIASES.put("py", "python");
        ALIASES.put("java", "java");
    }
    public static Set<String> normalizeSkills(String skills) {
        Set<String> normalizedSkills = new LinkedHashSet<>();
        if (skills == null || skills.trim().isEmpty()) {
            return normalizedSkills;
        }
        String[] skillList = skills.split(",");
        for (String skill : skillList) {
            skill = skill.trim().toLowerCase();
            if (skill.isEmpty()) {
                continue;
            }
            skill = ALIASES.getOrDefault(skill, skill);
            normalizedSkills.add(skill);
        }
        return normalizedSkills;
    }
    public static void main(String[] args) {
        String skills = " Java, JAVA, js, React.js, SQL, SQL ";
        Set<String> result = normalizeSkills(skills);
        System.out.println("Original Skills:");
        System.out.println(skills);
        System.out.println("\nNormalized Skills:");
        System.out.println(result);
    }
}