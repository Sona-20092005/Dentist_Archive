package com.dentistarchive.config.properties;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@ConfigurationProperties(prefix = "app")
@Validated
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AppProperties {

    @Valid
    @NotNull
    Jwt jwt = new Jwt();

    @Valid
    @NotNull
    SystemAdmin systemAdmin = new SystemAdmin();

    @NotEmpty
    List<@Valid LoginDelay> delaysAfterFailedLoginAttemptsForUsers = new ArrayList<>();

    public Duration getDelayByNumberOfFailedLoginAttemptsForUsers(int number) {
        return delaysAfterFailedLoginAttemptsForUsers.stream()
                .filter(it -> it.numberOfFails() <= number)
                .max(Comparator.comparingInt(LoginDelay::numberOfFails))
                .map(LoginDelay::delay)
                .orElse(Duration.ZERO);
    }

    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Jwt {
        @NotBlank
        String secret;

        @NotNull
        Duration accessTokenTtl = Duration.ofMinutes(15);

        @NotNull
        Duration refreshTokenTtl = Duration.ofDays(30);

        @NotBlank
        String issuer = "dentist-archive";
    }

    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class SystemAdmin {
        @NotBlank
        String userName = "admin";

        @NotBlank
        String fullName = "System Administrator";

        @NotBlank
        String password = "ChangeMe123!";
    }

    public record LoginDelay(
            @Positive
            int numberOfFails,
            @NotNull
            Duration delay
    ) {
    }
}