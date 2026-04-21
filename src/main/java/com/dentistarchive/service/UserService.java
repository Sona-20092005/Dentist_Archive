package com.dentistarchive.service;

import com.dentistarchive.config.properties.AppProperties;
import com.dentistarchive.entity.User;
import com.dentistarchive.exception.EntityNotFoundByIdException;
import com.dentistarchive.exception.actor.ManyFailedLoginAttemptsException;
import com.dentistarchive.repository.UserRepository;
import com.dentistarchive.search.filter.UserFilter;
import com.dentistarchive.security.AuthUtils;
import com.dentistarchive.utils.ClockUtils;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Service
@Validated
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {

    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    AppProperties appProperties;

    @Transactional(readOnly = true)
    public User getByIdWithoutAccessControlOrElseThrow(@NotNull UUID id) {
        return userRepository.getById(id)
                .orElseThrow(() -> new EntityNotFoundByIdException(User.class, id));
    }

    @Transactional(readOnly = true)
    public User getByLoginAndPassword(@NotBlank String login, @NotBlank String password) {
        UserFilter filter = UserFilter.builder()
                .login(login)
                .archived(false)
                .build();

        User user = searchOneWithoutAccessControl(filter).orElseThrow(() -> {
            log.error("Failed to find actor by login \"{}\"", login);
            return AuthUtils.getUnauthorizedException();
        });

        if (user.hasFailedLoginAttempts()) {
            Duration delay = appProperties.getDelayByNumberOfFailedLoginAttemptsForUsers(user.getNumberOfFailedLoginAttempts());
            if (delay.isPositive()) {
                OffsetDateTime now = ClockUtils.now();
                OffsetDateTime nextLoginAttemptAllowedAt = user.getLastLoginFailedAt().plus(delay);
                if (nextLoginAttemptAllowedAt.isAfter(now)) {
                    throw new ManyFailedLoginAttemptsException(Duration.between(now, nextLoginAttemptAllowedAt));
                }
            }
        }

        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            self().incrementNumberOfFailedLoginAttemptsInSeparateTransaction(user);
            throw AuthUtils.getUnauthorizedException();
        }

        if (user.hasFailedLoginAttempts()) {
            return self().clearNumberOfFailedLoginAttemptsInSeparateTransaction(user);
        }
        return user;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    protected User incrementNumberOfFailedLoginAttemptsInSeparateTransaction(User user) {
        user.incrementNumberOfFailedLoginAttempts();
        return userRepository.save(user);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    protected User clearNumberOfFailedLoginAttemptsInSeparateTransaction(User user) {
        user.clearNumberOfFailedLoginAttempts();
        return userRepository.save(user);
    }

    @Lookup
    protected UserService self() {
        return null;
    }

    private Optional<User> searchOneWithoutAccessControl(UserFilter filter) {
        List<User> entities = userRepository.search(filter);
        if (entities.size() > 1) {
            throw new ResponseStatusException(BAD_REQUEST,
                    "Found " + entities.size() + " actors by filter " + filter + ", but expected only 1");
        }
        return entities.stream().findFirst();
    }
}
