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
import org.springframework.util.unit.DataSize;
import org.springframework.validation.annotation.Validated;

import java.net.URLEncoder;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import static java.nio.charset.StandardCharsets.UTF_8;

@ConfigurationProperties(prefix = "app")
@Validated
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AppProperties {

    @NotNull
    @Valid
    EmailConfirmationProperties emailConfirmation;

    @NotBlank
    String usualUserRoleCode;

    @NotBlank
    String usualUserLoginUrl;

    @NotBlank
    String serviceProviderSetPasswordUrlFormat;

    @NotBlank
    String brokerSetPasswordUrlFormat;

    @NotEmpty
    List<@Valid LoginDelay> delaysAfterFailedLoginAttemptsForUsers = new ArrayList<>();

    @NotNull
    UserAvatarProperties userAvatar;

    /**
     * Optional property for cases when the file storage domain was changed after saving file url into the database
     */
    String publicFileStorageDomain;

    public String getServiceProviderSetPasswordUrl(String temporaryPassword) {
        return serviceProviderSetPasswordUrlFormat.formatted(URLEncoder.encode(temporaryPassword, UTF_8));
    }

    public String getBrokerSetPasswordUrl(String temporaryPassword) {
        return brokerSetPasswordUrlFormat.formatted( URLEncoder.encode(temporaryPassword, UTF_8));
    }

    public Duration getDelayByNumberOfFailedLoginAttemptsForUsers(int number) {
        return delaysAfterFailedLoginAttemptsForUsers.stream()
                .filter(it -> it.numberOfFails() <= number)
                .max(Comparator.comparingInt(LoginDelay::numberOfFails))
                .map(LoginDelay::delay)
                .orElse(Duration.ZERO);
    }

    public record EmailConfirmationProperties(
            @NotNull
            Duration oneTimeCodeLifeTime,
            @NotNull
            @Positive
            Integer codeLength,
            @NotNull
            @Positive
            Integer maxFailedAttempts
    ) {}

    public record LoginDelay(
            @Positive
            int numberOfFails,
            @NotNull
            Duration delay
    ) {}

    public record UserAvatarProperties(
            @NotNull
            DataSize maxSize,
            @NotEmpty
            Set<@NotBlank String> contentTypes
    ) {}
}
