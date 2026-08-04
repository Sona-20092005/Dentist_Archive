package com.dentistarchive.search.filter;

import com.dentistarchive.enums.NurseWorkType;
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
public class NurseWorkLogFilter extends ArchivingBaseFilter<NurseWorkLogFilter>{

    RangeFilter<OffsetDateTime> createdAt;

    RangeFilter<LocalDate> date;

    Set<NurseWorkType> workTypes;

    UUID nurseId;

    UUID clinicId;

    UUID doctorId;
}
