package com.dentistarchive.security.filter;


import com.dentistarchive.exception.InvalidAccessTokenException;
import com.dentistarchive.security.AccessTokenValidator;
import com.dentistarchive.security.CustomUserDetails;
import com.dentistarchive.security.UserDetailsConstants;
import com.dentistarchive.security.UserDetailsMapper;
import com.dentistarchive.utils.JwtUtils;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import static com.dentistarchive.utils.ErrorResponseUtils.buildErrorCodeResponse;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CustomJwtAuthenticationFilter implements WebFilter {

    UserDetailsMapper userDetailsMapper;
    AccessTokenValidator accessTokenValidator;
    ServerCodecConfigurer serverCodecConfigurer;

    @Override
    @NonNull
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String accessToken = JwtUtils.getAccessToken(exchange);
        if (accessToken == null) {
            return chain.filter(exchange)
                    .contextWrite(ReactiveSecurityContextHolder.clearContext());
        }
        CustomUserDetails userDetails;
        try {
            userDetails = accessTokenValidator.validateAndGetActorDetails(accessToken);
        } catch (InvalidAccessTokenException e) {
            return buildErrorCodeResponse(exchange, e.getErrorCode(), serverCodecConfigurer);
        }

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        ServerHttpRequest request = exchange.getRequest().mutate()
                .header(UserDetailsConstants.USER_DETAILS_HEADER_NAME, userDetailsMapper.serializeToBase64(userDetails))
                .build();

        return chain.filter(exchange.mutate().request(request).build())
                .contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication));
    }
}
