package com.dentistarchive.controller;

import com.nimbusds.jose.jwk.JWKMatcher;
import com.nimbusds.jose.jwk.JWKSelector;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.Writer;

@RestController
@Tag(name = "JWK Sets")
@RequestMapping("/api/v1/jwk")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JwkInternalController {

    JWKSource<SecurityContext> jwkSource;
    JWKSelector jwkSelector = new JWKSelector(new JWKMatcher.Builder().build());

    @GetMapping
    @SneakyThrows
    @Operation(hidden = true)
    public void getJwkSet(HttpServletResponse response) {
        JWKSet jwkSet = new JWKSet(this.jwkSource.get(this.jwkSelector, null));

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        try (Writer writer = response.getWriter()) {
            writer.write(jwkSet.toString());
        }
    }
}
