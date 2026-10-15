# TalentVela — Job Seeker Module (OMNI ARCH)

> **College Project:** Galgotias University (SCSE) — 3rd Semester Java Course  
> **Topic #9:** Online Job Portal  
> **Team Name:** OMNI ARCH  
> **Module Owner:** Kushagra Pandey (`feature/kushagra`)  
> **Target Review:** Review 1 (October 10, 2026)  

---

## 🎯 What This Module Contains

This module implements the **complete, end-to-end Job Seeker experience** for the **TalentVela** recruitment platform. It satisfies **Task 2** (Thymeleaf Screen System) and **Task 3** (Validation & Clickable Journey) from the team's master workflow.

Per the **No-Block Rule**, this module runs independently without requiring live MySQL connection or Spring Security auth. When Gagan's auth layer and Ashutosh's database connection are integrated on Day 4/5, these views simply connect to the real user session and JPA repositories.

---

## 🖥️ The Complete Job Seeker Screens (9 Views)

| Screen | Route | Description |
| :--- | :--- | :--- |
| **1. Dashboard** | `/seeker/dashboard` | Candidate welcome banner, profile strength bar, 4 key stats (Total Applied, Under Review, Shortlisted, Selected), top job matches with % scores, active resume widget, and recent applications. |
| **2. Explore Jobs** | `/seeker/jobs` | Live search by keyword/skill, city/state location filter, Work Mode (Remote/Hybrid/On-Site), and Job Type (Full-Time/Internship). Shows match scores and identifies applied / closed positions. |
| **3. Job Details** | `/seeker/jobs/{id}` | Full job specifications, salary range, deadline, and **Smart Skill Gap Analysis** (compares candidate skills vs. required skills). |
| **4. Apply Form** | `/seeker/jobs/{id}/apply` | Pre-fills candidate contact details, verifies attached resume, provides cover letter textarea, and blocks duplicate applications. |
| **5. My Applications** | `/seeker/applications` | Real-time application tracker table with color-coded status badges (`APPLIED`, `UNDER_REVIEW`, `SHORTLISTED`, `SELECTED`, `REJECTED`) and withdrawal capability. |
| **6. Application Details** | `/seeker/applications/{id}` | Dedicated application view with a **visual 4-stage stepper timeline** (*Applied → Under Review → Shortlisted → Selected*), cover note preview, and attached resume. |
| **7. Profile Overview** | `/seeker/profile` | Executive summary card, technical competency tags, academic qualifications, and profile completion strength checklist. |
| **8. Edit Profile** | `/seeker/profile/edit` | Dedicated edit form to modify personal details, bio, desired role, skills, college university, and career preferences. |
| **9. Resume & Intelligence** | `/seeker/resume` | Document upload area, active resume viewer, parsed skill preview, and **Market Readiness Index** benchmarking candidate skills against target roles. |

---

## 🚀 How to Run in IntelliJ IDEA

1. Open **IntelliJ IDEA**.
2. Select **Open** and choose this folder:
   `c:\Users\KUSHAGRA PANDEY\Desktop\UNIVERSITY\SEM 3\JAVA\ONLINE JOB PORTAL`
3. IntelliJ will detect `pom.xml` and automatically import dependencies via its bundled Maven.
4. Navigate to:
   `src/main/java/com/omniarch/talentvela/TalentVelaApplication.java`
5. Click the **Green Run Arrow (▶)** next to `main()`.
6. Open your web browser and visit:
   👉 **`http://localhost:8080/seeker/dashboard`**

---

## 🧱 Architectural Alignment

* **Design System:** Custom CSS (`/static/css/style.css`) — clean, responsive, modern card layout, accessible typography.
* **Templates:** Server-side rendered Thymeleaf templates with reusable layout fragments (`navbar.html`, `footer.html`).
* **Authoritative Schema:** All fields strictly match the reconciled database schema (`draft_schema2`) and seed data (`seed_data5`).
* **Recruitment Differentiator:** Implements explainable, transparent rule-based skill comparison and market readiness scoring.
