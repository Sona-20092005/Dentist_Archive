package com.dentistarchive.search.filter;

import com.dentistarchive.enums.PatientStatus;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.time.OffsetDateTime;
import java.util.Set;
import java.util.UUID;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PatientFilter extends ArchivingBaseFilter<PatientFilter>{

    RangeFilter<OffsetDateTime> createdAt;

    Set<PatientStatus> statuses;

    String nameContains;

    String phoneContains;

    String emailContains;

    UUID doctorId;
}
