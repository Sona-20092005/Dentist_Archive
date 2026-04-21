package com.dentistarchive.security;

import com.dentistarchive.enums.Role;
import com.dentistarchive.enums.TokenType;
import com.dentistarchive.service.JwtService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AccessTokenValidator {

    JwtService jwtService;

    public ValidatedAccessToken validate(String token) {
        if (token == null || token.isBlank()) {
            throw new BadCredentialsException("Token is missing");
        }

        if (!jwtService.isValid(token)) {
            throw new BadCredentialsException("Invalid token");
        }

        if (jwtService.extractTokenType(token) != TokenType.ACCESS) {
            throw new BadCredentialsException("Not an access token");
        }

        UUID userId = jwtService.extractUserId(token);
        Role role = jwtService.extractRole(token);

        return new ValidatedAccessToken(userId, role);
    }

    public record ValidatedAccessToken(
            UUID userId,
            Role role
    ) {}
}