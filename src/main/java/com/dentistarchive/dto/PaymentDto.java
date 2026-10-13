package com.dentistarchive.dto;

import com.dentistarchive.enums.PaymentMethod;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PROTECTED)
public class PaymentDto extends ArchivingBaseDto {

    LocalDate date;

    BigDecimal amount;

    PaymentMethod paymentMethod;

    BigDecimal commissionAmount = BigDecimal.ZERO;

    String notes;

    UUID patientId;
}
