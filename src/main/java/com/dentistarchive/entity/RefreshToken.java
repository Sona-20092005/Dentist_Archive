package com.dentistarchive.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "refresh_tokens")
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RefreshToken extends BaseEntity {

    @Column(nullable = false)
    UUID userId;

    @Column(nullable = false, unique = true, length = 500)
    String tokenHash;

    @Column(nullable = false)
    OffsetDateTime expiresAt;

    @Column(nullable = false)
    boolean revoked;

    OffsetDateTime revokedAt;

    String revokeReason;

    public boolean isExpired(OffsetDateTime now) {
        return expiresAt != null && now.isAfter(expiresAt);
    }

    public boolean isActive(OffsetDateTime now) {
        return !revoked && !isExpired(now);
    }

    public void revoke(String reason, OffsetDateTime now) {
        this.revoked = true;
        this.revokedAt = now;
        this.revokeReason = reason;
    }
}