package com.dentistarchive.search.filter;

import com.dentistarchive.enums.RentCalculationType;
import com.dentistarchive.enums.RentMonthlyReportStatus;
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
public class RentMonthlyReportFilter extends ArchivingBaseFilter<RentMonthlyReportFilter> {

    RangeFilter<OffsetDateTime> createdAt;

    Set<RentMonthlyReportStatus> statuses;

    Set<RentCalculationType> calculationTypes;

    RangeFilter<Integer> year;

    UUID clinicId;

    UUID clinicRentAgreementId;

    UUID doctorId;

}
