package com.dentistarchive.search.filter;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WorkScheduleFilter extends ArchivingBaseFilter<WorkScheduleFilter>{

    RangeFilter<OffsetDateTime> createdAt;

    RangeFilter<LocalDate> effectiveFrom;

    RangeFilter<LocalDate> effectiveUntil;

    String nameContains;

    UUID clinicId;

    UUID doctorId;
}
