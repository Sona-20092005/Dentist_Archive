package com.dentistarchive.security;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthHolder {

    public static Optional<UUID> getUserId() {
        return getUserDetails().map(CustomUserDetails::getUserId);
    }

    public static UUID getUserIdOrElseThrow() {
        return getUserId().orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));
    }

    public static Optional<CustomUserDetails> getUserDetails() {
        return Optional.of(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .map(Authentication::getPrincipal)
                .filter(CustomUserDetails.class::isInstance)
                .map(it -> (CustomUserDetails) it);
    }
}
