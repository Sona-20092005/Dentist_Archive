package com.dentistarchive.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

import static java.util.stream.Collectors.toSet;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ActorDetails implements UserDetails {

    @JsonProperty("aid")
    UUID actorId;

    @JsonProperty("s")
    String scope;

    @JsonProperty("u")
    boolean isUser;

    @JsonIgnore
    String passwordHash;

    @JsonProperty("ext")
    boolean externalSystemClient;

    @JsonProperty("p")
    @Builder.Default
    Set<String> permissionCodes = new HashSet<>();

    @JsonProperty("l")
    Locale locale;

    @JsonProperty("cid")
    UUID companyId;

    @JsonIgnore
    public boolean isClient() {
        return !isUser;
    }

    @Override
    @JsonIgnore
    public Collection<GrantedAuthority> getAuthorities() {
        return permissionCodes.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(toSet());
    }

    @Override
    @JsonIgnore
    public String getPassword() {
        return passwordHash;
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }
}
