package com.dentistarchive.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.dentistarchive.exception.ErrorCode;
import com.dentistarchive.exception.InvalidAccessTokenException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;

import java.util.List;

import static org.springframework.util.CollectionUtils.isEmpty;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JwtUtils {

    private static final String BEARER_AUTH_PREFIX = "Bearer ";

    public static String getAccessToken(ServerWebExchange exchange) {
        List<String> authorizationHeaderValues = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION);
        if (isEmpty(authorizationHeaderValues)) {
            return null;
        }
        if (authorizationHeaderValues.size() > 1) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        String authorizationHeaderValue = authorizationHeaderValues.getFirst().trim();
        if (!authorizationHeaderValue.startsWith(BEARER_AUTH_PREFIX)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        return authorizationHeaderValue.replaceFirst(BEARER_AUTH_PREFIX, "").trim();
    }

    public static String getActorIdFromAccessToken(ServerWebExchange exchange) {
        String accessToken = getAccessToken(exchange);
        if (accessToken == null) {
            return null;
        }
        DecodedJWT jwt;
        try {
            jwt = JWT.decode(accessToken);
        } catch (JWTDecodeException e) {
            throw new InvalidAccessTokenException(ErrorCode.ACCESS_TOKEN_FORMAT_INVALID.getCode());
        }
        String actorId = jwt.getClaim("actorId").as(String.class);
        if (actorId == null) {
            log.error("Access token without actorId received");
        }
        return actorId;
    }
}
