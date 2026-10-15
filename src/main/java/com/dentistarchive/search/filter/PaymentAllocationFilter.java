package com.dentistarchive.search.filter;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaymentAllocationFilter extends ArchivingBaseFilter<PaymentAllocationFilter>{

    RangeFilter<OffsetDateTime> createdAt;

    UUID treatmentPlanId;

    UUID completedTreatmentId;

    UUID paymentId;

    UUID doctorId;
}
