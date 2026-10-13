package com.dentistarchive.dto.create;

import com.dentistarchive.dto.PaymentDto;
import com.dentistarchive.enums.PaymentMethod;
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
public class PaymentCreateDto extends BaseCreateDto<PaymentDto>{

    LocalDate date;

    BigDecimal amount;

    PaymentMethod paymentMethod;

    BigDecimal commissionAmount;

    String notes;

    UUID patientId;

}
