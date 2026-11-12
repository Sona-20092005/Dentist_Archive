package com.dentistarchive.dto.update;

import com.dentistarchive.dto.TechnicianDto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.FieldNameConstants;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@FieldNameConstants
public class TechnicianUpdateDto extends BaseUpdateDto<TechnicianDto> {

    @NotNull
    String name;

    List<@NotBlank String> phones;

    List<@NotBlank @Email String> emails;

    String notes;

    LocalDate collaborationStartDate;

    LocalDate collaborationEndDate;

    @NotNull
    LocalDate monthlyReportStartDate;

    String address;
}
