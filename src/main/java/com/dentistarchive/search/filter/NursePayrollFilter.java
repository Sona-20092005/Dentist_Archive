package com.dentistarchive.search.filter;

import com.dentistarchive.enums.CompensationType;
import com.dentistarchive.enums.NursePayrollStatus;
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
public class NursePayrollFilter extends ArchivingBaseFilter<NursePayrollFilter>{

    RangeFilter<OffsetDateTime> createdAt;

    Set<NursePayrollStatus> statuses;

    RangeFilter<Integer> year;

    RangeFilter<LocalDate>  paymentDate;

    Set<CompensationType> compensationType;

    Boolean isManuallyAdjusted;

    UUID nurseId;

    UUID clinicId;

    UUID doctorId;

}
