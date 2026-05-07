package com.dentistarchive.entity;

import com.dentistarchive.enums.ArchiveStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.MappedSuperclass;
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
@MappedSuperclass
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PROTECTED)
public abstract class ArchivableBaseEntity extends MutableBaseEntity {

    @Enumerated(EnumType.STRING)
    ArchiveStatus archiveStatus;

    OffsetDateTime archivedAt;

    UUID archivedBy;

    public boolean isArchived() {
        return archiveStatus != ArchiveStatus.ACTIVE;
    }
}
