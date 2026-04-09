package com.dentistarchive.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.dentistarchive.config.properties.AuthServerProperties;
import com.dentistarchive.dto.auth.UserWithPermissionsDto;
import com.dentistarchive.utils.ClockUtils;
import com.nimbusds.jose.jwk.JWKMatcher;
import com.nimbusds.jose.jwk.JWKSelector;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.security.interfaces.ECPrivateKey;
import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JwtService {

    AuthServerProperties authServerProperties;
    AuthServerUserMapper userMapper;
    UserDetailsMapper userDetailsMapper;
    JWKSource<SecurityContext> jwkSource;

    JWKSelector jwkSelector = new JWKSelector(new JWKMatcher.Builder().build());

    public String generateAccessToken(UserWithPermissionsDto userWithPermissionsDto) {
        OffsetDateTime now = ClockUtils.now();
        CustomUserDetails userDetails = userMapper.toUserDetails(userWithPermissionsDto);
        return JWT.create()
                .withIssuedAt(now.toInstant())
                .withExpiresAt(now.plus(
                        authServerProperties.getUserAccessTokenLifeTime()
                ).toInstant())
                .withPayload(userDetailsMapper.serializeToJson(userDetails))
                .sign(Algorithm.ECDSA256(getECPrivateKey()));
    }

    @SneakyThrows
    private ECPrivateKey getECPrivateKey() {
        JWKSet jwkSet = new JWKSet(jwkSource.get(jwkSelector, null));
        return jwkSet.getKeys().getFirst().toECKey().toECPrivateKey();
    }
}
