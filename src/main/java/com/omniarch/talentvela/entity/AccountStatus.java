package com.omniarch.talentvela.entity;

/**
 * Represents the account lifecycle status of a user.
 */
public enum AccountStatus {
    ACTIVE,
    PENDING_VERIFICATION,
    SUSPENDED,
    LOCKED;

    /**
     * Determines whether the user account is allowed to log in and access protected resources.
     *
     * @return true if the account is in an active usable state
     */
    public boolean canAuthenticate() {
        return this == ACTIVE;
    }
}
