package com.dentistarchive.dto.create;

import com.dentistarchive.dto.PatientDto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.FieldNameConstants;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@FieldNameConstants
public class PatientCreateDto extends BaseCreateDto<PatientDto>{

    @NotBlank
    String name;

    List<@NotBlank String> phones;

    List<@NotBlank @Email String> emails;

    String address;

    String passportInformation;

    String notes;

}
