package com.dentistarchive.dto.update;

import com.dentistarchive.dto.NurseDto;
import com.dentistarchive.dto.create.BaseCreateDto;
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

    String name;

    BigDecimal baseSalary;

    BigDecimal hourlyRate;

    LocalDate hireDate;

    LocalDate terminationDate;

    LocalDate payrollStartDate;

    List<String> phones;

    List<String> emails;

    String notes;

    UUID clinicId;
}
