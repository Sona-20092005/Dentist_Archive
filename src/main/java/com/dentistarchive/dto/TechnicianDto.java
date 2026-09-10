package com.dentistarchive.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PROTECTED)
public class TechnicianDto extends ArchivingBaseDto {

    String name;

    List<String> phones;

    List<String> emails;

    String notes;

    String address;

    LocalDate collaborationStartDate;

    LocalDate collaborationEndDate;

    LocalDate monthlyReportStartDate;

    UUID doctorId;

}


