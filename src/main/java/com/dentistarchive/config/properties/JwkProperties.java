package com.dentistarchive.config.properties;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;

import java.util.List;

@ConfigurationProperties("auth-server.jwk")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JwkProperties {

    List<JwkKey> keys = List.of();

    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class JwkKey {
        String keyId;
        Resource privateKeyPath;
        Resource publicKeyPath;
    }
}
