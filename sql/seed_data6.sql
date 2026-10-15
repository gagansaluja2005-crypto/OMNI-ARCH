-- OMNI ARCH - REVISED SEED DATA (Matched to ERD Schema)
USE omniarch_db;

-- 1. Insert Users (1 Admin, 2 Employers, 3 Seekers)
INSERT INTO USER (full_name, email, phone, password_hash, role, account_status) VALUES
('System Admin', 'admin@omniarch.com', '1111111111', 'dummyhash123', 'ADMIN', 'ACTIVE'),           
('TechCorp HR', 'hr@techcorp.com', '2222222222', 'dummyhash123', 'EMPLOYER', 'ACTIVE'),           
('InnoSoft HR', 'jobs@innosoft.com', '3333333333', 'dummyhash123', 'EMPLOYER', 'ACTIVE'),         
('Alice Smith', 'alice.seeker@email.com', '9876543210', 'dummyhash123', 'JOB_SEEKER', 'ACTIVE'),  
('Bob Johnson', 'bob.worker@email.com', '8765432109', 'dummyhash123', 'JOB_SEEKER', 'ACTIVE'),    
('Charlie Brown', 'charlie.dev@email.com', '7654321098', 'dummyhash123', 'JOB_SEEKER', 'ACTIVE');   

-- 2. Insert Employer Profiles (Referencing user_id 2 and 3)
INSERT INTO EMPLOYER_PROFILE (user_id, company_name, industry, company_size, founded_year, contact_person, company_email, company_phone, address_line, city, state, country, pin_zip, website, about_company) VALUES
(2, 'TechCorp', 'IT/Software', '50-200', 2015, 'TechCorp HR', 'hr@techcorp.com', '2222222222', '123 Tech Park', 'Bengaluru', 'Karnataka', 'India', '560001', 'www.techcorp.com', 'Leading tech company in AI'),
(3, 'InnoSoft', 'IT/Software', '200-500', 2010, 'InnoSoft HR', 'jobs@innosoft.com', '3333333333', '456 IT Hub', 'Pune', 'Maharashtra', 'India', '411001', 'www.innosoft.com', 'Enterprise software solutions');

-- 3. Insert Job Seeker Profiles (Referencing user_id 4, 5, 6)
INSERT INTO JOB_SEEKER_PROFILE (user_id, headline, bio, city, state, country, desired_role, experience_years, skills, highest_qualification, expected_salary) VALUES
(4, 'Java Backend Engineer', 'Passionate about scalable systems', 'Delhi', 'Delhi', 'India', 'Backend Developer', 3, 'Java, Spring Boot, MySQL', 'B.Tech', '1200000'),
(5, 'Frontend Developer', 'Crafting beautiful UIs', 'Mumbai', 'Maharashtra', 'India', 'UI Engineer', 2, 'React, Node.js, MongoDB', 'MCA', '800000'),
(6, 'Data Analyst', 'Making sense of data', 'Gurugram', 'Haryana', 'India', 'Data Scientist', 1, 'Python, Data Analysis, SQL', 'B.Sc', '600000');

-- 4. Insert Jobs (employer_id references EMPLOYER_PROFILE.id, so 1 and 2)
INSERT INTO JOB (employer_id, title, description, required_skills, experience_required, salary_min, salary_max, job_type, work_mode, openings, application_deadline, location_city, location_state, location_country, status) VALUES
(1, 'Backend Java Developer', 'We need a strong Java backend dev.', 'Java, Spring Boot', '3+ Years', 1000000, 1500000, 'Full-Time', 'Hybrid', 2, '2026-11-01', 'Bengaluru', 'Karnataka', 'India', 'PUBLISHED'),
(1, 'Database Administrator', 'Looking for MySQL expert.', 'MySQL, Performance Tuning', '5+ Years', 1200000, 1800000, 'Full-Time', 'On-Site', 1, '2026-11-15', 'Bengaluru', 'Karnataka', 'India', 'PUBLISHED'),
(2, 'Frontend Developer', 'Looking for React specialist.', 'React, JavaScript, CSS', '2+ Years', 800000, 1200000, 'Full-Time', 'Remote', 3, '2026-10-31', 'Pune', 'Maharashtra', 'India', 'PUBLISHED'),
(2, 'Fullstack Engineer', 'MERN stack developer needed.', 'Node.js, React, MongoDB', '4+ Years', 1500000, 2000000, 'Full-Time', 'Hybrid', 1, '2026-11-20', 'Pune', 'Maharashtra', 'India', 'PENDING');

-- 5. Insert Resumes (job_seeker_id references JOB_SEEKER_PROFILE.id, so 1, 2, 3)
INSERT INTO RESUME (job_seeker_id, file_name, file_path, file_type, extracted_skills) VALUES
(1, 'alice_resume.pdf', '/uploads/resumes/alice.pdf', 'application/pdf', 'Java, Spring Boot'),
(2, 'bob_cv.pdf', '/uploads/resumes/bob.pdf', 'application/pdf', 'React, Node.js'),
(3, 'charlie_data.pdf', '/uploads/resumes/charlie.pdf', 'application/pdf', 'Python, SQL');

-- 6. Insert Applications
INSERT INTO APPLICATION (job_id, job_seeker_id, resume_id, cover_letter_text, status) VALUES
(1, 1, 1, 'I am great at Java backend dev.', 'APPLIED'),       
(3, 2, 2, 'React is my passion.', 'SHORTLISTED'),   
(2, 3, 3, 'I optimize databases.', 'UNDER_REVIEW'),  
(4, 2, 2, 'Ready for fullstack.', 'APPLIED'),
(2, 1, 1, 'I would be a great fit for the DBA role.', 'SELECTED');
