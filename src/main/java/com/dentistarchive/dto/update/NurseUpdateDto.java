package com.dentistarchive.dto.update;

import com.dentistarchive.dto.NurseDto;
import com.dentistarchive.dto.create.BaseCreateDto;
import com.dentistarchive.enums.CompensationType;
import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.FieldNameConstants;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@FieldNameConstants
public class NurseUpdateDto extends BaseCreateDto<NurseDto> {

    @NotBlank
    String name;

    @PositiveOrZero
    BigDecimal baseSalary;

    @PositiveOrZero
    BigDecimal hourlyRate;

    @PositiveOrZero
    BigDecimal overtimeHourlyRate;

    LocalDate hireDate;

    LocalDate terminationDate;

    @NotNull
    LocalDate payrollStartDate;

    @Positive
    Integer contractedWeeklyHours;

    @NotNull
    CompensationType compensationType;

    List<@NotBlank String> phones;

    List<@NotBlank @Email String> emails;

    String notes;

    @NotNull
    UUID clinicId;
}
