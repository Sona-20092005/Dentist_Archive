package com.dentistarchive.config.properties;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;

@ConfigurationProperties(prefix = "auth-server")
@Validated
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthServerProperties {

    @NotNull
    Duration userAccessTokenLifeTime;
    @NotNull
    Duration sessionMaxInactiveTime;

}
