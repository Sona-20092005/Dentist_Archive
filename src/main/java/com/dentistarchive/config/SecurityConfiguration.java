package com.dentistarchive.config;

import com.dentistarchive.repository.jpa.DoctorJpaRepository;
import com.dentistarchive.security.UserDetailsMapper;

import com.dentistarchive.security.filter.SessionIdFilter;
import com.dentistarchive.security.filter.UserDetailsAuthenticationFilter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import tools.jackson.databind.ObjectMapper;

import java.util.stream.Stream;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, UserDetailsMapper userDetailsMapper) throws Exception {
        http.authorizeHttpRequests(requests -> requests
                        .requestMatchers(
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/actuator/**",
                                "/api/auth/users/access-and-refresh-tokens",
                                "/api/auth/users/access-and-refresh-tokens/refresh"
                        ).permitAll()
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/v1/announcements/*",
                                "/api/v1/announcements/addresses/suggestions"
                        ).permitAll()
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/v1/announcements/search",
                                "/api/v1/announcements/history/search"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(new UserDetailsAuthenticationFilter(userDetailsMapper), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new SessionIdFilter(), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exceptionHandling ->
                        exceptionHandling.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)));
        return http.build();
    }

//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(csrf -> csrf.disable())
//                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .authorizeHttpRequests(auth -> auth
//                        // swagger open
//                        .requestMatchers(
//                                "/swagger-ui.html",
//                                "/swagger-ui/**",
//                                "/v3/api-docs/**",
//                                "/api/auth/users/access-and-refresh-tokens",
//                                "/api/auth/users/access-and-refresh-tokens/refresh"
//                        ).permitAll()
//
//                        // secure the rest
//                        .anyRequest().authenticated()
//                );
//             //   .httpBasic(b -> {});
//
//        return http.build();
//    }

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

    @Bean
    @ConditionalOnMissingBean
    public UserDetailsMapper actorDetailsMapper(ObjectMapper objectMapper) {
        return new UserDetailsMapper(objectMapper);
    }
}