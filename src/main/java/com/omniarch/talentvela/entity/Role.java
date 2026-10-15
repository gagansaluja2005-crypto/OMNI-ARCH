package com.omniarch.talentvela.entity;

/**
 * Defines the roles available within the system.
 * - JOB_SEEKER: Candidates looking for jobs, managing applications, resumes, and profiles.
 * - EMPLOYER: Hiring managers/recruiters posting jobs, reviewing candidates, and managing company profiles.
 * - ADMIN: System administrators managing users, roles, platform configuration, and audit logs.
 *
 * Public registration allows self-signup ONLY as JOB_SEEKER or EMPLOYER.
 * ADMIN accounts must be provisioned internally through privileged administrator endpoints.
 */
public enum Role {
    JOB_SEEKER,
    EMPLOYER,
    ADMIN;

    /**
     * Checks if this role can be selected during public self-registration.
     *
     * @return true if allowed for public self-registration, false otherwise
     */
    public boolean isPublicRegistrationAllowed() {
        return this == JOB_SEEKER || this == EMPLOYER;
    }

    /**
     * Returns the Spring Security authority representation (e.g. ROLE_JOB_SEEKER).
     *
     * @return authority string prefixed with ROLE_
     */
    public String toAuthority() {
        return "ROLE_" + this.name();
    }
}
