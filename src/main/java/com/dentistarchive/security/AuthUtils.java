package com.dentistarchive.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.dentistarchive.dto.auth.AuthResponseDto;
import com.dentistarchive.dto.auth.UserWithPermissionsDto;
import com.dentistarchive.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.session.Session;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.time.ZoneOffset;
import java.util.Collection;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthUtils {

    public static boolean hasPermission(String permissionCode) {
        return AuthHolder.getUserDetailsOrElseThrow().getPermissionCodes().contains(permissionCode);
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

    public static void assertThatSessionStartedAfterLastPasswordChange(
            UserWithPermissionsDto actorWithPermissions,
            Session session,
            Consumer<Session> sessionInvalidator
    ) {
        // all sessions must be invalidated after password change
        if (session.getCreationTime().isBefore(actorWithPermissions.getPasswordSetAt().toInstant())) {
            sessionInvalidator.accept(session);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, ErrorCode.REFRESH_TOKEN_INVALIDATED.getCode());
        }
    }

    public static AuthResponseDto buildAuthResponseDtoWithoutRefreshToken(String accessTokenValue) {
        DecodedJWT accessToken = JWT.decode(accessTokenValue);
        return AuthResponseDto.builder()
                .accessToken(accessTokenValue)
                .accessTokenIssuedAt(requireNonNull(accessToken.getIssuedAt()).toInstant().atOffset(ZoneOffset.UTC))
                .accessTokenExpiresAt(requireNonNull(accessToken.getExpiresAt()).toInstant().atOffset(ZoneOffset.UTC))
                .build();
    }

    public static AuthResponseDto addRefreshTokenToAuthResponseDto(
            AuthResponseDto authResponseDto,
            String tokenValue,
            Duration timeToLive
    ) {
        return authResponseDto.toBuilder()
                .refreshToken(tokenValue)
                .refreshTokenIssuedAt(authResponseDto.getAccessTokenIssuedAt())
                .refreshTokenExpiresAt(authResponseDto.getAccessTokenIssuedAt().plus(timeToLive))
                .build();
    }
}
