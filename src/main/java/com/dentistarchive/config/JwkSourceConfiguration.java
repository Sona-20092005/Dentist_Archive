package com.dentistarchive.config;

import com.dentistarchive.config.properties.JwkProperties;
import com.nimbusds.jose.jwk.Curve;
import com.nimbusds.jose.jwk.ECKey;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import lombok.SneakyThrows;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.InputStreamReader;
import java.security.KeyPair;
import java.security.interfaces.ECPublicKey;
import java.util.List;

@Configuration
public class JwkSourceConfiguration {

    @Bean
    JWKSource<SecurityContext> jwkSource(JwkProperties jwkProperties) {
        List<JWK> ecKeys = jwkProperties.getKeys().stream()
                .map(jwkKey -> {
                    KeyPair keyPair = readKeyPair(jwkKey.getPrivateKeyPath(), jwkKey.getPublicKeyPath());
                    return (JWK) new ECKey.Builder(Curve.P_256, (ECPublicKey) keyPair.getPublic())
                            .privateKey(keyPair.getPrivate())
                            .keyID(jwkKey.getKeyId())
                            .build();
                })
                .toList();
        JWKSet jwkSet = new JWKSet(ecKeys);
        return (jwkSelector, securityContext) -> jwkSelector.select(jwkSet);
    }

    @SneakyThrows
    private KeyPair readKeyPair(Resource privateKeyResource, Resource publicKeyResource) {
        try (
                InputStreamReader privateKeyReader = new InputStreamReader(privateKeyResource.getInputStream());
                InputStreamReader publicKeyReader = new InputStreamReader(publicKeyResource.getInputStream())
        ) {
            PEMParser privatePemParser = new PEMParser(privateKeyReader);
            PrivateKeyInfo privateKeyInfo = (PrivateKeyInfo) privatePemParser.readObject();

            PEMParser publicPemParser = new PEMParser(publicKeyReader);
            SubjectPublicKeyInfo publicKeyInfo = (SubjectPublicKeyInfo) publicPemParser.readObject();

            PEMKeyPair pemKeyPair = new PEMKeyPair(publicKeyInfo, privateKeyInfo);
            JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
            return converter.getKeyPair(pemKeyPair);
        }
    }
}
