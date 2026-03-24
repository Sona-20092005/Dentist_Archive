package com.dentistarchive.entity;

import com.dentistarchive.security.AuthHolder;
import com.dentistarchive.utils.ClockUtils;
import com.dentistarchive.utils.UUIDGenerator;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@SuperBuilder
@MappedSuperclass
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PROTECTED)
public abstract class BaseEntity implements Serializable {

    @Id
    @Column(nullable = false, unique = true)
    UUID id;

    @Column(nullable = false)
    OffsetDateTime createdAt;

    UUID createdBy;

    @PrePersist
    protected void prePersist() {
        if (id == null) {
            id = UUIDGenerator.generateUUIDv7();
        }
        createdAt = ClockUtils.now();
        if (createdBy == null) {
            createdBy = AuthHolder.getActorId().orElse(null);
        }
    }
}
