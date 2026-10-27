package com.dentistarchive.dto.create;

import com.dentistarchive.dto.ToothConditionNoteDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.FieldNameConstants;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@FieldNameConstants
public class ToothConditionNoteCreateDto extends BaseCreateDto<ToothConditionNoteDto>{

    @NotNull
    Integer toothNumber;

    @NotBlank
    String description;

    @NotNull
    UUID patientId;

}
