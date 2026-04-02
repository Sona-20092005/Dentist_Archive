package com.dentistarchive.security;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthHolder {

    public static Optional<UUID> getActorId() {
        return getActorDetails().map(ActorDetails::getActorId);
    }

    public static UUID getActorIdOrElseThrow() {
        return getActorId().orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));
    }

    public static Optional<ActorDetails> getActorDetails() {
        return Optional.of(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .map(Authentication::getPrincipal)
                .filter(ActorDetails.class::isInstance)
                .map(it -> (ActorDetails) it);
    }

    public static UserDetails getActorDetailsOrElseThrow() {
        return getActorDetails().orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));
    }

    public static void setAuthentication(UserDetails actorDetails) {
        if (actorDetails == null) {
            clearAuthentication();
            return;
        }
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(actorDetails, null, actorDetails.getAuthorities()));
    }

    public static void clearAuthentication() {
        SecurityContextHolder.getContext().setAuthentication(null);
    }
}
