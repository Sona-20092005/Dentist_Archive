package com.dentistarchive.dto.create;

import com.dentistarchive.dto.ClinicDto;
import com.dentistarchive.enums.PaymentMethod;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.FieldNameConstants;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@FieldNameConstants
public class ClinicCreateDto extends BaseCreateDto<ClinicDto>{

    @NotBlank
    String name;

    List<@NotBlank String> phones;

    List<@NotBlank @Email String> emails;

    String address;

    @DecimalMin("0.00")
    @DecimalMax("100.00")
    BigDecimal commissionPercent;

    List<PaymentMethod> commissionPaymentMethods;

    String notes;
}
