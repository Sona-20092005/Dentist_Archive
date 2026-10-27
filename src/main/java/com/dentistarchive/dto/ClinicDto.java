package com.dentistarchive.dto;

import com.dentistarchive.enums.PaymentMethod;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PROTECTED)
public class ClinicDto extends ArchivingBaseDto {

    String name;

    List<String> phones;

    List<String> emails;

    String address;

    BigDecimal commissionPercent;

    List<PaymentMethod> commissionPaymentMethods;

    String notes;

    UUID doctorId;
}


