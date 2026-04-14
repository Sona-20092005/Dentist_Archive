package com.dentistarchive.security.filter;

import com.dentistarchive.security.SessionHolder;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

public class SessionIdFilter extends OncePerRequestFilter {

    public static final String REAL_ESTATE_SESSION_ID_HEADER = "RE-Session-Id";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        String sessionId = request.getHeader(REAL_ESTATE_SESSION_ID_HEADER);
        if (StringUtils.isEmpty(sessionId)) {
            chain.doFilter(request, response);
            return;
        }
        try {
            SessionHolder.setSessionId(UUID.fromString(sessionId));
            chain.doFilter(request, response);
        } finally {
            SessionHolder.removeSessionId();
        }
    }
}
