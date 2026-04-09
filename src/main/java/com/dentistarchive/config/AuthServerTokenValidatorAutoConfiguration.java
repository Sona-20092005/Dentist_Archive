package com.dentistarchive.config;

import com.dentistarchive.security.AccessTokenValidator;
import com.dentistarchive.security.UserDetailsMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
public class AuthServerTokenValidatorAutoConfiguration {

    @Bean
    @ConditionalOnProperty(name = "app.security.jwk.url")
    AccessTokenValidator accessTokenValidator(@Value("${app.security.jwk.url}") String jwkUrl,
                                              UserDetailsMapper userDetailsMapper) {
        return new AccessTokenValidator(jwkUrl, userDetailsMapper);
    }
}
