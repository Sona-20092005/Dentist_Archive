package com.dentistarchive.auth;

import com.dentistarchive.auth.dto.AuthResponse;
import com.dentistarchive.auth.dto.LoginRequest;
import com.dentistarchive.config.properties.AppProperties;
import com.dentistarchive.entity.User;
import com.dentistarchive.repository.UserRepository;
import com.dentistarchive.security.CustomUserDetails;
import com.dentistarchive.security.UserDetailsMapper;
import com.dentistarchive.service.JwtService;
import com.dentistarchive.service.RefreshTokenService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthService {

    AuthenticationManager authenticationManager;
    JwtService jwtService;
    RefreshTokenService refreshTokenService;
    UserRepository userRepository;
    AppProperties appProperties;
    UserDetailsMapper userDetailsMapper;

    @Transactional
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByUserName(request.userName())
                .orElseThrow(() -> new BadCredentialsException("Invalid username or password"));

        validateLoginDelay(user);

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.userName(),
                            request.password()
                    )
            );

            CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();

            user.clearNumberOfFailedLoginAttempts();
            userRepository.save(user);

            String accessToken = jwtService.generateAccessToken(principal);
            String refreshToken = refreshTokenService.createAndSaveRefreshToken(user);

            return new AuthResponse(
                    accessToken,
                    jwtService.extractExpiration(accessToken),
                    refreshToken,
                    jwtService.extractExpiration(refreshToken)
            );
        } catch (DisabledException ex) {
            throw ex;
        } catch (Exception ex) {
            user.incrementNumberOfFailedLoginAttempts();
            userRepository.save(user);
            throw new BadCredentialsException("Invalid username or password", ex);
        }
    }

    @Transactional
    public AuthResponse refresh(String rawRefreshToken) {
        User user = refreshTokenService.validateAndGetUser(rawRefreshToken);

        CustomUserDetails principal = userDetailsMapper.toPrincipal(user);

        String newAccessToken = jwtService.generateAccessToken(principal);
        String newRefreshToken = refreshTokenService.rotateRefreshToken(rawRefreshToken);

        return new AuthResponse(
                newAccessToken,
                jwtService.extractExpiration(newAccessToken),
                newRefreshToken,
                jwtService.extractExpiration(newRefreshToken)
        );
    }

    @Transactional
    public void logout(String rawRefreshToken) {
        refreshTokenService.revokeRefreshToken(rawRefreshToken, "LOGOUT");
    }

    private void validateLoginDelay(User user) {
        if (!user.hasFailedLoginAttempts() || user.getLastLoginFailedAt() == null) {
            return;
        }

        var delay = appProperties.getDelayByNumberOfFailedLoginAttemptsForUsers(
                user.getNumberOfFailedLoginAttempts()
        );

        var allowedAt = user.getLastLoginFailedAt().plus(delay);
        var now = com.dentistarchive.utils.ClockUtils.now();

        if (now.isBefore(allowedAt)) {
            throw new BadCredentialsException("Too many failed login attempts. Try again later.");
        }
    }
}