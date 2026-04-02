package com.dentistarchive.security;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.stream.Stream;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthUtils {

    public static boolean hasPermission(String permissionCode) {
        return AuthHolder.getActorDetailsOrElseThrow().getPermissionCodes().contains(permissionCode);
    }

    public static boolean hasNoPermission(String permissionCode) {
        return !hasPermission(permissionCode);
    }

    public static boolean hasAllPermissions(String... permissionCodes) {
        return Stream.of(permissionCodes).allMatch(AuthUtils::hasPermission);
    }

    public static boolean hasAllPermissions(Collection<String> permissionCodes) {
        return permissionCodes.stream().allMatch(AuthUtils::hasPermission);
    }

    public static boolean hasAnyPermission(String... permissionCodes) {
        return Stream.of(permissionCodes).anyMatch(AuthUtils::hasPermission);
    }

    public static void throwAccessDeniedException() {
        throwAccessDeniedException(null);
    }

    public static void throwAccessDeniedException(String errorCode) {
        throw getAccessDeniedException(errorCode);
    }

    public static AccessDeniedException getAccessDeniedException() {
        return getAccessDeniedException(null);
    }

    public static AccessDeniedException getAccessDeniedException(String errorCode) {
        return new AccessDeniedException(errorCode != null ? errorCode : "Forbidden");
    }

    public static void throwUnauthorizedException() {
        throw getUnauthorizedException();
    }

    public static ResponseStatusException getUnauthorizedException() {
        return new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
    }

    public static ResponseStatusException getUnauthorizedException(String errorCode, Object... args) {
        return new ResponseStatusException(HttpStatus.UNAUTHORIZED, errorCode);
    }
}
