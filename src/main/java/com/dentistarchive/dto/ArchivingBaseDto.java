package com.dentistarchive.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
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
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PROTECTED)
public abstract class ArchivingBaseDto extends MutableBaseDto {

    @Schema(description = "Entity is archived")
    boolean archived;

    @Schema(description = "Last archiving date and time")
    OffsetDateTime archivedAt;

    @Schema(description = "Id of actor who archived entity last time")
    UUID archivedBy;

    @JsonIgnore
    public boolean isNotArchived() {
        return !archived;
    }
}
