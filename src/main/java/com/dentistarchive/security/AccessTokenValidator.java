package com.dentistarchive.security;

import com.auth0.jwk.Jwk;
import com.auth0.jwk.JwkProvider;
import com.auth0.jwk.JwkProviderBuilder;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.dentistarchive.exception.ErrorCode;
import com.dentistarchive.exception.InvalidAccessTokenException;
import com.dentistarchive.utils.ClockUtils;
import lombok.AccessLevel;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.security.interfaces.ECPublicKey;
import java.util.Base64;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static java.nio.charset.StandardCharsets.UTF_8;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AccessTokenValidator {

    UserDetailsMapper userDetailsMapper;
    JwkProvider jwkProvider;

    @SneakyThrows
    public AccessTokenValidator(String jwkUrl, UserDetailsMapper actorDetailsMapper) {
        this.userDetailsMapper = actorDetailsMapper;
        jwkProvider = new JwkProviderBuilder(URI.create(jwkUrl).toURL())
                .cached(10, 24, TimeUnit.HOURS)
                .rateLimited(10, 1, TimeUnit.MINUTES)
                .build();
    }

    @SneakyThrows
    public CustomUserDetails validateAndGetActorDetails(String token) {
        DecodedJWT jwt;
        try {
            jwt = JWT.decode(token);
            Jwk jwk = jwkProvider.get(jwt.getKeyId());
            Algorithm algorithm = Algorithm.ECDSA256((ECPublicKey) jwk.getPublicKey(), null);
            algorithm.verify(jwt);
        } catch (JWTDecodeException e) {
            throw new InvalidAccessTokenException(ErrorCode.ACCESS_TOKEN_FORMAT_INVALID.getCode());
        } catch (SignatureVerificationException e) {
            throw new InvalidAccessTokenException(ErrorCode.ACCESS_TOKEN_SIGNATURE_INVALID.getCode());
        }

        if (jwt.getExpiresAt().before(new Date(ClockUtils.now().toInstant().toEpochMilli()))) {
            throw new InvalidAccessTokenException(ErrorCode.ACCESS_TOKEN_EXPIRED.getCode());
        }
        return toUserDetails(jwt);
    }

    private CustomUserDetails toUserDetails(DecodedJWT jwt) {
        try {
            String payload = new String(Base64.getUrlDecoder().decode(jwt.getPayload().getBytes(UTF_8)), UTF_8);
            return userDetailsMapper.deserializeFromJson(payload);
        } catch (Exception e) {
            log.error("Failed to deserialize JWT payload {}", jwt.getPayload(), e);
            throw e;
        }
    }
}
