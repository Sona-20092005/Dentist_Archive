package com.dentistarchive.security.filter;

import com.dentistarchive.entity.User;
import com.dentistarchive.repository.UserRepository;
import com.dentistarchive.security.AccessTokenValidator;
import com.dentistarchive.security.CustomUserDetails;
import com.dentistarchive.security.UserDetailsMapper;
import com.dentistarchive.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    JwtService jwtService;
    UserRepository userRepository;
    AccessTokenValidator accessTokenValidator;
    UserDetailsMapper userDetailsMapper;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String authorizationHeader = request.getHeader("Authorization");

        if (!StringUtils.hasText(authorizationHeader) || !authorizationHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authorizationHeader.substring(7);

        try {
            if (!jwtService.isValid(token) || !jwtService.isAccessToken(token)) {
                filterChain.doFilter(request, response);
                return;
            }

            var validated = accessTokenValidator.validate(token);

            UUID userId = validated.userId();

            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                User user = userRepository.getById(userId).orElse(null);

                if (user != null && !user.isArchived()) {
                    CustomUserDetails principal =
                            userDetailsMapper.toPrincipal(user, true);

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    principal,
                                    null,
                                    principal.getAuthorities()
                            );

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        } catch (Exception ignored) {
        }

        filterChain.doFilter(request, response);
    }
}