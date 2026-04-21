package com.dentistarchive.security;

import com.dentistarchive.enums.Role;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SecurityManager {

    public CustomUserDetails getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !(authentication.getPrincipal() instanceof CustomUserDetails)) {
            return null;
        }

        return (CustomUserDetails) authentication.getPrincipal();
    }

    public UUID getCurrentUserId() {
        CustomUserDetails user = getCurrentUser();
        return user != null ? user.getUserId() : null;
    }

    public boolean isAuthenticated() {
        return getCurrentUser() != null;
    }

    public boolean hasRole(Role role) {
        CustomUserDetails user = getCurrentUser();
        return user != null && user.getRole() == role;
    }

    public void requireAuthenticated() {
        if (!isAuthenticated()) {
            throw new IllegalStateException("User is not authenticated");
        }
    }

    public void requireRole(Role role) {
        CustomUserDetails user = getCurrentUser();

        if (user == null || user.getRole() != role) {
            throw new IllegalStateException("Access denied");
        }
    }
}