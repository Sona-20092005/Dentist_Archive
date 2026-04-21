package com.dentistarchive.service;

import com.dentistarchive.config.properties.AppProperties;
import com.dentistarchive.entity.RefreshToken;
import com.dentistarchive.entity.User;
import com.dentistarchive.repository.UserRepository;
import com.dentistarchive.repository.jpa.RefreshTokenJpaRepository;
import com.dentistarchive.security.CustomUserDetails;
import com.dentistarchive.security.UserDetailsMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.HexFormat;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RefreshTokenService {

    RefreshTokenJpaRepository refreshTokenRepository;
    UserRepository userRepository;
    JwtService jwtService;
    AppProperties appProperties;
    UserDetailsMapper userDetailsMapper;

    @Transactional
    public String createAndSaveRefreshToken(User user) {
        CustomUserDetails principal = userDetailsMapper.toPrincipal(user);


        String rawRefreshToken = jwtService.generateRefreshToken(principal);

        RefreshToken refreshToken = RefreshToken.builder()
                .userId(user.getId())
                .tokenHash(hash(rawRefreshToken))
                .expiresAt(OffsetDateTime.now(ZoneOffset.UTC).plus(appProperties.getJwt().getRefreshTokenTtl()))
                .revoked(false)
                .build();

        refreshTokenRepository.save(refreshToken);

        return rawRefreshToken;
    }

    @Transactional(readOnly = true)
    public User validateAndGetUser(String rawRefreshToken) {
        if (!jwtService.isValid(rawRefreshToken)) {
            throw new BadCredentialsException("Invalid refresh token");
        }

        if (!jwtService.isRefreshToken(rawRefreshToken)) {
            throw new BadCredentialsException("Token is not a refresh token");
        }

        String hash = hash(rawRefreshToken);

        RefreshToken storedToken = refreshTokenRepository.findByTokenHash(hash)
                .orElseThrow(() -> new BadCredentialsException("Refresh token not found"));

        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);

        if (storedToken.isRevoked()) {
            throw new BadCredentialsException("Refresh token revoked");
        }

        if (storedToken.isExpired(now)) {
            throw new BadCredentialsException("Refresh token expired");
        }

        UUID userIdFromJwt = jwtService.extractUserId(rawRefreshToken);

        if (!storedToken.getUserId().equals(userIdFromJwt)) {
            throw new BadCredentialsException("Refresh token user mismatch");
        }

        return userRepository.getById(storedToken.getUserId())
                .orElseThrow(() -> new BadCredentialsException("User not found"));
    }

    @Transactional
    public String rotateRefreshToken(String oldRawRefreshToken) {
        String oldHash = hash(oldRawRefreshToken);

        RefreshToken oldStoredToken = refreshTokenRepository.findByTokenHash(oldHash)
                .orElseThrow(() -> new BadCredentialsException("Refresh token not found"));

        User user = validateAndGetUser(oldRawRefreshToken);

        oldStoredToken.revoke("ROTATED", OffsetDateTime.now(ZoneOffset.UTC));
        refreshTokenRepository.save(oldStoredToken);

        return createAndSaveRefreshToken(user);
    }

    @Transactional
    public void revokeRefreshToken(String rawRefreshToken, String reason) {
        String tokenHash = hash(rawRefreshToken);

        refreshTokenRepository.findByTokenHash(tokenHash)
                .ifPresent(token -> {
                    token.revoke(reason, OffsetDateTime.now(ZoneOffset.UTC));
                    refreshTokenRepository.save(token);
                });
    }

    @Transactional
    public void revokeAllUserRefreshTokens(UUID userId, String reason) {
        List<RefreshToken> tokens = refreshTokenRepository.findAllByUserIdAndRevokedFalse(userId);
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);

        for (RefreshToken token : tokens) {
            token.revoke(reason, now);
        }

        refreshTokenRepository.saveAll(tokens);
    }

    private String hash(String value) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] bytes = digest.digest(value.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(bytes);
        } catch (Exception ex) {
            throw new IllegalStateException("Failed to hash refresh token", ex);
        }
    }
}