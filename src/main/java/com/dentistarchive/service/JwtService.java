package com.dentistarchive.service;

import com.dentistarchive.config.properties.AppProperties;
import com.dentistarchive.enums.Role;
import com.dentistarchive.enums.TokenType;
import com.dentistarchive.security.CustomUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JwtService {

    static final String CLAIM_ROLE = "role";
    static final String CLAIM_TYPE = "type";

    AppProperties appProperties;

    public String generateAccessToken(CustomUserDetails principal) {
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        OffsetDateTime expiresAt = now.plus(appProperties.getJwt().getAccessTokenTtl());

        return Jwts.builder()
                .subject(principal.getUserId().toString())
                .issuer(appProperties.getJwt().getIssuer())
                .issuedAt(Date.from(now.toInstant()))
                .expiration(Date.from(expiresAt.toInstant()))
                .claim(CLAIM_ROLE, principal.getRole().name())
                .claim(CLAIM_TYPE, TokenType.ACCESS.name())
                .signWith(getSigningKey())
                .compact();
    }

    public String generateRefreshToken(CustomUserDetails principal) {
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        OffsetDateTime expiresAt = now.plus(appProperties.getJwt().getRefreshTokenTtl());

        return Jwts.builder()
                .subject(principal.getUserId().toString())
                .issuer(appProperties.getJwt().getIssuer())
                .issuedAt(Date.from(now.toInstant()))
                .expiration(Date.from(expiresAt.toInstant()))
                .claim(CLAIM_ROLE, principal.getRole().name())
                .claim(CLAIM_TYPE, TokenType.REFRESH.name())
                .signWith(getSigningKey())
                .compact();
    }

    public boolean isValid(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public UUID extractUserId(String token) {
        return UUID.fromString(parseClaims(token).getSubject());
    }

    public Role extractRole(String token) {
        String value = parseClaims(token).get(CLAIM_ROLE, String.class);
        return Role.valueOf(value);
    }

    public TokenType extractTokenType(String token) {
        String value = parseClaims(token).get(CLAIM_TYPE, String.class);
        return TokenType.valueOf(value);
    }

    public OffsetDateTime extractExpiration(String token) {
        Date expiration = parseClaims(token).getExpiration();
        return OffsetDateTime.ofInstant(expiration.toInstant(), ZoneOffset.UTC);
    }

    public boolean isAccessToken(String token) {
        return extractTokenType(token) == TokenType.ACCESS;
    }

    public boolean isRefreshToken(String token) {
        return extractTokenType(token) == TokenType.REFRESH;
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(appProperties.getJwt().getSecret().getBytes(StandardCharsets.UTF_8));
    }
}