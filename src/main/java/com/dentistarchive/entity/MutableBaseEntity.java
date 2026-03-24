package com.dentistarchive.entity;

import com.dentistarchive.security.AuthHolder;
import com.dentistarchive.utils.ClockUtils;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PreUpdate;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@MappedSuperclass
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PROTECTED)
public abstract class MutableBaseEntity extends BaseEntity {

    OffsetDateTime updatedAt;

    UUID updatedBy;

    @PreUpdate
    protected void preUpdate() {
        updatedAt = ClockUtils.now();
        updatedBy = AuthHolder.getActorId().orElse(null);
    }
}
