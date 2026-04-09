package com.dentistarchive.controller;

import com.dentistarchive.config.properties.AuthServerProperties;
import com.dentistarchive.dto.auth.AuthResponseDto;
import com.dentistarchive.dto.auth.LoginAndPasswordDto;
import com.dentistarchive.dto.auth.LoginRequestDto;
import com.dentistarchive.dto.auth.RefreshTokenDto;
import com.dentistarchive.entity.User;
import com.dentistarchive.mapper.UserMapper;
import com.dentistarchive.security.AuthUtils;
import com.dentistarchive.security.JwtService;
import com.dentistarchive.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.session.Session;
import org.springframework.session.SessionRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.service.annotation.DeleteExchange;

import java.util.Optional;
import java.util.UUID;

@RestController
@Tag(name = "User Auth", description = "Users authentication")
@RequestMapping("/api/auth/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthController {

    private static final String ACTOR_ID_SESSION_ATTRIBUTE_NAME = "actorId";

    JwtService jwtService;
    AuthServerProperties authServerProperties;
    SessionRepository sessionRepository;
    UserService userService;
    UserMapper userMapper;

    @PostMapping("/access-and-refresh-tokens")
    @Operation(
            summary = "Get user access and refresh tokens using login and password",
            description = "Returns access and refresh tokens"
    )
    public AuthResponseDto getUserAccessAndRefreshTokens(
            @RequestBody @Valid LoginRequestDto requestDto,
            HttpServletRequest httpServletRequest
    ) {
        HttpSession httpSession = httpServletRequest.getSession(false);
        if (httpSession != null) {
            httpSession.invalidate(); // invalidate previous session if it exists
        }

        var loginAndPasswordDto = new LoginAndPasswordDto(requestDto.getLogin(), requestDto.getPassword(), requestDto.getScope());

        User user = userService.getByLoginAndPassword(requestDto.getLogin(), requestDto.getPassword(), requestDto.getScope());
        var actorWithPermissionsDto = userMapper.toWithPermissionsDto(user);

        Session session = sessionRepository.createSession();
        session.setAttribute(ACTOR_ID_SESSION_ATTRIBUTE_NAME, actorWithPermissionsDto.getUser().getId());
        session.setMaxInactiveInterval(authServerProperties.getSessionMaxInactiveTime());
        sessionRepository.save(session);

        var authResponseDto = AuthUtils.buildAuthResponseDtoWithoutRefreshToken(jwtService.generateAccessToken(actorWithPermissionsDto));
        return AuthUtils.addRefreshTokenToAuthResponseDto(
                authResponseDto,
                session.getId(),
                session.getMaxInactiveInterval()
        );
    }

    @PostMapping("/access-and-refresh-tokens/refresh")
    @Operation(
            summary = "Refresh user access and refresh tokens",
            description = "Consumes refresh token and produces new access token"
    )
    public AuthResponseDto refreshUserAccessAndRefreshTokens(
            @RequestBody @Valid RefreshTokenDto requestDto,
            HttpServletRequest httpServletRequest
    ) {
        // check that request doesn't contain session cookie
        if (httpServletRequest.getRequestedSessionId() != null) {
            throw new IllegalStateException("This request must be made without linked session");
        }

        // search session by refresh token
        Session session = Optional.ofNullable(sessionRepository.findById(requestDto.getRefreshToken()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));
        var actorWithPermissionsDto = Optional.of(session)
                .map(it -> it.getAttribute(ACTOR_ID_SESSION_ATTRIBUTE_NAME))
                .map(Object::toString)
                .map(UUID::fromString)
                .map(userService::getByIdWithoutAccessControlOrElseThrow)
                .map(userMapper::toWithPermissionsDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));
        AuthUtils.assertThatSessionStartedAfterLastPasswordChange(
                actorWithPermissionsDto,
                session,
                s -> sessionRepository.deleteById(s.getId())
        );

        // change session id (and refresh token)
        session.changeSessionId();
        sessionRepository.save(session);

        // generate response
        String accessToken = jwtService.generateAccessToken(actorWithPermissionsDto);
        AuthResponseDto responseDto = AuthUtils.buildAuthResponseDtoWithoutRefreshToken(accessToken);
        return AuthUtils.addRefreshTokenToAuthResponseDto(
                responseDto,
                session.getId(),
                session.getMaxInactiveInterval()
        );
    }

    @DeleteExchange("/refresh-tokens/revoke")
    @Operation(summary = "Revoke refresh token (access token remains active till the end of its lifetime)")
    public void revokeRefreshToken(
            @RequestBody @Valid RefreshTokenDto requestDto,
            HttpServletRequest httpServletRequest
    ) {
        // check that request doesn't contain session cookie
        if (httpServletRequest.getRequestedSessionId() != null) {
            throw new IllegalStateException("This request must be made without linked session");
        }

        Session session = Optional.ofNullable(sessionRepository.findById(requestDto.getRefreshToken()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));
        sessionRepository.deleteById(session.getId());
    }
}
