package com.omniarch.talentvela;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TalentVelaApplication {

    public static void main(String[] args) {
        SpringApplication.run(TalentVelaApplication.class, args);
        System.out.println("=================================================");
        System.out.println("🚀 TalentVela (OMNI ARCH) started successfully!");
        System.out.println("🌐 Open in browser: http://localhost:8080/seeker/dashboard");
        System.out.println("=================================================");
    }
}
