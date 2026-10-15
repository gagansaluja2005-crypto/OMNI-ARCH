-- OMNI ARCH - TASK 3 (corrected against draft_schema2)
USE omniarch_db;

-- ==========================================
-- 1. INDEXES (duplicate-application constraint removed — 
--    already exists inline in draft_schema2)
-- ==========================================
CREATE INDEX idx_user_email ON USER(email);
CREATE INDEX idx_job_status ON JOB(status);
CREATE INDEX idx_job_employer_id ON JOB(employer_id);
CREATE INDEX idx_job_location ON JOB(location_city, location_state);
CREATE INDEX idx_application_job_id ON APPLICATION(job_id);
CREATE INDEX idx_application_seeker_id ON APPLICATION(job_seeker_id);
CREATE INDEX idx_application_status ON APPLICATION(status);

-- ==========================================
-- 2. CRUD VERIFICATION
-- ==========================================

-- CREATE
INSERT INTO USER (full_name, email, password_hash, role) 
VALUES ('New Employer', 'new_employer@test.com', 'hash', 'EMPLOYER');
SET @new_user_id = LAST_INSERT_ID();
INSERT INTO EMPLOYER_PROFILE (user_id, company_name, city) 
VALUES (@new_user_id, 'Test Company', 'Delhi');

-- READ
SELECT u.email, e.company_name, e.city 
FROM USER u JOIN EMPLOYER_PROFILE e ON u.id = e.user_id 
WHERE u.id = @new_user_id;

-- UPDATE
UPDATE JOB SET status = 'CLOSED' WHERE id = 1;

-- DELETE
-- DELETE FROM APPLICATION WHERE id = 1;

-- ==========================================
-- 3. BUSINESS QUERIES
-- (employer_id / job_seeker_id values below assume seed_data3 — 
--  TechCorp = EMPLOYER_PROFILE.id 1, Alice = JOB_SEEKER_PROFILE.id 1)
-- ==========================================

-- Q1: Employer's jobs
SELECT title, location_city, status, created_at 
FROM JOB WHERE employer_id = 1;

-- Q2: Pending jobs
SELECT j.id, j.title, e.company_name, j.created_at 
FROM JOB j JOIN EMPLOYER_PROFILE e ON j.employer_id = e.id 
WHERE j.status = 'PENDING';

-- Q3: Published jobs
SELECT title, company_name, salary_min, salary_max, location_city 
FROM JOB j JOIN EMPLOYER_PROFILE e ON j.employer_id = e.id
WHERE j.status = 'PUBLISHED';

-- Q4: Applicants for a job (joins through USER for the name, 
-- since first_name/last_name no longer exist on the profile)
SELECT a.id, u.full_name, a.status, a.applied_at 
FROM APPLICATION a
JOIN JOB_SEEKER_PROFILE s ON a.job_seeker_id = s.id
JOIN USER u ON s.user_id = u.id
WHERE a.job_id = 1;

-- Q5: One candidate's applications
SELECT j.title, e.company_name, a.status, a.applied_at 
FROM APPLICATION a
JOIN JOB j ON a.job_id = j.id
JOIN EMPLOYER_PROFILE e ON j.employer_id = e.id
WHERE a.job_seeker_id = 1;

-- Q6: Applications by status
SELECT a.id, j.title, u.full_name, a.status 
FROM APPLICATION a
JOIN JOB j ON a.job_id = j.id
JOIN JOB_SEEKER_PROFILE s ON a.job_seeker_id = s.id
JOIN USER u ON s.user_id = u.id
WHERE a.status = 'SHORTLISTED';

-- ==========================================
-- 4. EMPLOYER DASHBOARD NUMBERS (Employer Profile ID 1 = TechCorp)
-- ==========================================
SELECT COUNT(*) AS total_posted_jobs FROM JOB WHERE employer_id = 1;

SELECT COUNT(*) AS pending_jobs FROM JOB WHERE employer_id = 1 AND status = 'PENDING';

SELECT COUNT(a.id) AS total_applications 
FROM APPLICATION a JOIN JOB j ON a.job_id = j.id 
WHERE j.employer_id = 1;

SELECT COUNT(a.id) AS shortlisted_candidates 
FROM APPLICATION a JOIN JOB j ON a.job_id = j.id 
WHERE j.employer_id = 1 AND a.status = 'SHORTLISTED';

SELECT COUNT(a.id) AS selected_candidates 
FROM APPLICATION a JOIN JOB j ON a.job_id = j.id 
WHERE j.employer_id = 1 AND a.status = 'SELECTED';
