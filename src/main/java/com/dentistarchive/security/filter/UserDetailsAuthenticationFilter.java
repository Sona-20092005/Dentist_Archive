package com.dentistarchive.security.filter;

import com.dentistarchive.security.CustomUserDetails;
import com.dentistarchive.security.UserDetailsConstants;
import com.dentistarchive.security.UserDetailsMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserDetailsAuthenticationFilter extends OncePerRequestFilter {

    UserDetailsMapper userDetailsMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        String header = request.getHeader(UserDetailsConstants.USER_DETAILS_HEADER_NAME);
        if (StringUtils.isEmpty(header)) {
            chain.doFilter(request, response);
            return;
        }
        try {
            CustomUserDetails userDetails = userDetailsMapper.deserializeFromBase64(header);
            SecurityContextHolder.getContext().setAuthentication(
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()));
            chain.doFilter(request, response);
        } finally {
            SecurityContextHolder.clearContext();
        }
    }
}
