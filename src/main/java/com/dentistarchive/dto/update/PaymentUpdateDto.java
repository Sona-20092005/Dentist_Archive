package com.dentistarchive.dto.update;

import com.dentistarchive.dto.PaymentDto;
import com.dentistarchive.enums.PaymentMethod;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.FieldNameConstants;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@FieldNameConstants
public class PaymentUpdateDto extends BaseUpdateDto<PaymentDto> {

    @NotNull
    LocalDate date;

    @NotNull
    @Positive
    BigDecimal amount;

    @NotNull
    PaymentMethod paymentMethod;

    @NotNull
    @PositiveOrZero
    BigDecimal commissionAmount;

    String notes;

    @NotNull
    UUID patientId;
}
