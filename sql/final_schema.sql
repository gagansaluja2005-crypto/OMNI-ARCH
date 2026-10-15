-- OMNI ARCH - FINAL RECONCILED SCHEMA (Oct 5)
-- ERD Matched with Chitransh's final fields.

CREATE DATABASE IF NOT EXISTS omniarch_db;
USE omniarch_db;

-- 1. USER
CREATE TABLE USER (
    id INT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    phone VARCHAR(50),
    password_hash VARCHAR(255) NOT NULL,
    role ENUM('ADMIN', 'EMPLOYER', 'JOB_SEEKER') NOT NULL,
    account_status ENUM('ACTIVE', 'INACTIVE') DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 2. JOB_SEEKER_PROFILE
CREATE TABLE JOB_SEEKER_PROFILE (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL UNIQUE,
    profile_photo VARCHAR(255),
    headline VARCHAR(255),
    bio TEXT,
    city VARCHAR(100),
    state VARCHAR(100),
    country VARCHAR(100),
    desired_role VARCHAR(100),
    experience_years INT,
    skills TEXT,
    highest_qualification VARCHAR(100),
    college_university VARCHAR(255),
    field_of_study VARCHAR(100),
    graduation_year YEAR,
    preferred_location VARCHAR(100),
    preferred_work_mode VARCHAR(50),
    expected_salary VARCHAR(100),
    notice_period VARCHAR(50),
    FOREIGN KEY (user_id) REFERENCES USER(id) ON DELETE CASCADE
);

-- 3. EMPLOYER_PROFILE
CREATE TABLE EMPLOYER_PROFILE (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL UNIQUE,
    company_name VARCHAR(255) NOT NULL,
    company_logo VARCHAR(255),
    company_description TEXT,
    industry VARCHAR(100),
    company_size VARCHAR(50),
    founded_year YEAR,
    contact_person VARCHAR(255),
    company_email VARCHAR(255),
    company_phone VARCHAR(50),
    address_line VARCHAR(255),
    city VARCHAR(100),
    state VARCHAR(100),
    country VARCHAR(100),
    pin_zip VARCHAR(20),
    website VARCHAR(255),
    linkedin_url VARCHAR(255),
    about_company TEXT,
    benefits TEXT,
    FOREIGN KEY (user_id) REFERENCES USER(id) ON DELETE CASCADE
);

-- 4. JOB
CREATE TABLE JOB (
    id INT AUTO_INCREMENT PRIMARY KEY,
    employer_id INT NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    required_skills TEXT,
    experience_required VARCHAR(50),
    salary_min DECIMAL(10,2),
    salary_max DECIMAL(10,2),
    job_type VARCHAR(50),
    work_mode VARCHAR(50),
    openings INT,
    application_deadline DATE,
    location_address VARCHAR(255),
    location_city VARCHAR(100),
    location_state VARCHAR(100),
    location_country VARCHAR(100),
    status ENUM('DRAFT', 'PENDING', 'APPROVED', 'REJECTED', 'CLOSED', 'PUBLISHED') DEFAULT 'DRAFT',
    rejection_reason TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (employer_id) REFERENCES EMPLOYER_PROFILE(id) ON DELETE CASCADE
);

-- 5. RESUME
CREATE TABLE RESUME (
    id INT AUTO_INCREMENT PRIMARY KEY,
    job_seeker_id INT NOT NULL UNIQUE,
    file_name VARCHAR(255) NOT NULL,
    file_path VARCHAR(255) NOT NULL,
    file_type VARCHAR(50),
    extracted_text TEXT,
    extracted_skills TEXT,
    uploaded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (job_seeker_id) REFERENCES JOB_SEEKER_PROFILE(id) ON DELETE CASCADE
);

-- 6. APPLICATION
CREATE TABLE APPLICATION (
    id INT AUTO_INCREMENT PRIMARY KEY,
    job_id INT NOT NULL,
    job_seeker_id INT NOT NULL,
    resume_id INT,
    cover_letter_text TEXT,
    cover_letter_path VARCHAR(255),
    applied_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status ENUM('APPLIED', 'UNDER_REVIEW', 'SHORTLISTED', 'REJECTED', 'SELECTED') DEFAULT 'APPLIED',
    status_updated_at TIMESTAMP NULL,
    rejection_reason TEXT,
    UNIQUE KEY unique_job_seeker_application (job_id, job_seeker_id),
    FOREIGN KEY (job_id) REFERENCES JOB(id) ON DELETE CASCADE,
    FOREIGN KEY (job_seeker_id) REFERENCES JOB_SEEKER_PROFILE(id) ON DELETE CASCADE,
    FOREIGN KEY (resume_id) REFERENCES RESUME(id) ON DELETE SET NULL
);

-- 7. MESSAGE
CREATE TABLE MESSAGE (
    id INT AUTO_INCREMENT PRIMARY KEY,
    sender_user_id INT NOT NULL,
    receiver_user_id INT NOT NULL,
    job_id INT,
    application_id INT,
    subject VARCHAR(255),
    message_body TEXT NOT NULL,
    attachment_path VARCHAR(255),
    sent_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    read_at TIMESTAMP NULL,
    FOREIGN KEY (sender_user_id) REFERENCES USER(id) ON DELETE CASCADE,
    FOREIGN KEY (receiver_user_id) REFERENCES USER(id) ON DELETE CASCADE,
    FOREIGN KEY (job_id) REFERENCES JOB(id) ON DELETE SET NULL,
    FOREIGN KEY (application_id) REFERENCES APPLICATION(id) ON DELETE SET NULL
);

-- 8. ACTIVITY_LOG
CREATE TABLE ACTIVITY_LOG (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    action VARCHAR(255) NOT NULL,
    module VARCHAR(100),
    reference_type VARCHAR(100),
    reference_id INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES USER(id) ON DELETE CASCADE
);

-- 9. SYSTEM_SETTING
CREATE TABLE SYSTEM_SETTING (
    id INT AUTO_INCREMENT PRIMARY KEY,
    setting_key VARCHAR(100) UNIQUE NOT NULL,
    setting_value VARCHAR(255) NOT NULL,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
