package com.dentistarchive.config.properties;

import jakarta.validation.Valid;
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

    @NotEmpty
    List<@Valid LoginDelay> delaysAfterFailedLoginAttemptsForUsers = new ArrayList<>();


    public Duration getDelayByNumberOfFailedLoginAttemptsForUsers(int number) {
        return delaysAfterFailedLoginAttemptsForUsers.stream()
                .filter(it -> it.numberOfFails() <= number)
                .max(Comparator.comparingInt(LoginDelay::numberOfFails))
                .map(LoginDelay::delay)
                .orElse(Duration.ZERO);
    }


    public record LoginDelay(
            @Positive
            int numberOfFails,
            @NotNull
            Duration delay
    ) {
    }

}
