package com.omniarch.talentvela.dto.response;

import com.omniarch.talentvela.entity.AccountStatus;
import com.omniarch.talentvela.entity.Role;
import com.omniarch.talentvela.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Session user representation returned to clients upon successful authentication.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SessionUser {

    private Long id;
    private String fullName;
    private String email;
    private String phone;
    private Role role;
    private AccountStatus accountStatus;

    public SessionUser() {
    }

    public SessionUser(Long id, String fullName, String email, String phone, Role role, AccountStatus accountStatus) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.role = role;
        this.accountStatus = accountStatus;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static SessionUser fromUser(User user) {
        if (user == null) {
            return null;
        }
        return SessionUser.builder()
                .id(user.getId() != null ? user.getId().longValue() : null)
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .role(user.getRole())
                .accountStatus(user.getAccountStatus())
                .build();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public AccountStatus getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(AccountStatus accountStatus) {
        this.accountStatus = accountStatus;
    }

    public static class Builder {
        private Long id;
        private String fullName;
        private String email;
        private String phone;
        private Role role;
        private AccountStatus accountStatus;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder fullName(String fullName) {
            this.fullName = fullName;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder phone(String phone) {
            this.phone = phone;
            return this;
        }

        public Builder role(Role role) {
            this.role = role;
            return this;
        }

        public Builder accountStatus(AccountStatus accountStatus) {
            this.accountStatus = accountStatus;
            return this;
        }

        public SessionUser build() {
            return new SessionUser(id, fullName, email, phone, role, accountStatus);
        }
    }
}
