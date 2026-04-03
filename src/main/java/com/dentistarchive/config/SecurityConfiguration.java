package com.dentistarchive.config;

import com.dentistarchive.repository.jpa.DoctorJpaRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import java.util.stream.Stream;

@Configuration
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // swagger open
                        .requestMatchers(
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/v3/api-docs/**"
                        ).permitAll()

                        // secure the rest
                        .anyRequest().authenticated()
                )
                .httpBasic(b -> {});

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(DoctorJpaRepository doctorJpaRepository) {
        return username -> doctorJpaRepository.findByNickName(username)
                .map(doctor -> {
                    var roleAuthorities = doctor.getRoles() == null ? Stream.<SimpleGrantedAuthority>empty()
                            : doctor.getRoles().stream().flatMap(role -> {
                                var roleCode = new SimpleGrantedAuthority("ROLE_" + role.getCode().toUpperCase());
                                var perms = role.getPermissionCodes() == null ? Stream.<SimpleGrantedAuthority>empty()
                                        : role.getPermissionCodes().stream().map(SimpleGrantedAuthority::new);
                                return Stream.concat(Stream.of(roleCode), perms);
                            });
                    var authorities = roleAuthorities.toList();
                    return User.withUsername(doctor.getNickName())
                            .password(doctor.getPasswordHash())
                            .authorities(authorities)
                            .accountLocked(false)
                            .disabled(false)
                            .build();
                })
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }
}