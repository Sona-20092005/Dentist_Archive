package com.dentistarchive.dto;

import com.dentistarchive.enums.PaymentAllocationArchiveReason;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PROTECTED)
public class PaymentAllocationDto extends ArchivingBaseDto {

    UUID paymentId;

    BigDecimal amount;

    UUID treatmentPlanId;

    UUID completedTreatmentId;

    PaymentAllocationArchiveReason archiveReason;

}
