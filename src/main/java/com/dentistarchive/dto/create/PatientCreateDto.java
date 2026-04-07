package com.dentistarchive.dto.create;

import com.dentistarchive.dto.PatientDto;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.FieldNameConstants;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.UUID;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@FieldNameConstants
public class PatientCreateDto extends BaseCreateDto<PatientDto>{

    String name;

    List<String> phones;

    List<String> emails;

    String address;

    String passportInformation;

    String notes;

    UUID doctorId;

}
