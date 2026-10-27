package com.dentistarchive.dto.update;

import com.dentistarchive.dto.PaymentAllocationDto;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.FieldNameConstants;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@FieldNameConstants
public class PaymentAllocationUpdateDto extends BaseUpdateDto<PaymentAllocationDto> {

    @NotNull
    @Positive
    BigDecimal amount;

    UUID treatmentPlanId;

    UUID completedTreatmentId;
}
