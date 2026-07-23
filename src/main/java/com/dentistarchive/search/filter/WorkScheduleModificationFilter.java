package com.dentistarchive.search.filter;

import com.dentistarchive.enums.ModificationType;
import com.dentistarchive.enums.WorkSessionType;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Set;
import java.util.UUID;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WorkScheduleModificationFilter extends ArchivingBaseFilter<WorkScheduleModificationFilter>{

    RangeFilter<OffsetDateTime> createdAt;

    RangeFilter<LocalDate> date;

    Set<ModificationType> modificationTypes;

    Set<WorkSessionType> workSessionTypes;

    UUID scheduleId;

    UUID doctorId;

    UUID clinicId;
}
