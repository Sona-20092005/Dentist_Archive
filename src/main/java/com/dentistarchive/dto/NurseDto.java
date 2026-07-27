package com.dentistarchive.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PROTECTED)
public class NurseDto extends ArchivingBaseDto {

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


