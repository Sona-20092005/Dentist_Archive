package com.dentistarchive.search.filter;

import com.dentistarchive.enums.TechnicianMonthlyReportStatus;
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
public class TechnicianMonthlyReportFilter extends ArchivingBaseFilter<TechnicianMonthlyReportFilter>{

    RangeFilter<OffsetDateTime> createdAt;

    Set<TechnicianMonthlyReportStatus> statuses;

    RangeFilter<Integer> year;

    UUID technicianId;

    UUID doctorId;

}
