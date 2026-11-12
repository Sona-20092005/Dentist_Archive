package com.dentistarchive.dto.update;

import com.dentistarchive.dto.MaterialDto;
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
public class MaterialUpdateDto extends BaseUpdateDto<MaterialDto> {

    @NotBlank
    String name;

    String description;

    String notes;

    @NotNull
    UUID categoryId;
}
