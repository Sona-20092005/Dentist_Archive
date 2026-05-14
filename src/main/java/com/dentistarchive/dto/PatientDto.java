package com.dentistarchive.dto;

import com.dentistarchive.enums.PatientStatus;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.UUID;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PROTECTED)
public class PatientDto extends ArchivingBaseDto {

    String name;

    PatientStatus patientStatus;

    List<String> phones;

    List<String> emails;

    String address;

    String passportInformation;

    String notes;

    UUID doctorId;

}
