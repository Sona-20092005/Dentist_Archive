package com.dentistarchive.security;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.server.ResponseStatusException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthUtils {

    public static AccessDeniedException getAccessDeniedException() {
        return getAccessDeniedException(null);
    }

    public static AccessDeniedException getAccessDeniedException(String errorCode) {
        return new AccessDeniedException(errorCode != null ? errorCode : "Forbidden");
    }

    public static ResponseStatusException getUnauthorizedException() {
        return new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
    }

}
