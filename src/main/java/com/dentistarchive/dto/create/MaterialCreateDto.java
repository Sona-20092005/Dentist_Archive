package com.dentistarchive.dto.create;

import com.dentistarchive.dto.MaterialDto;
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
public class MaterialCreateDto extends BaseCreateDto<MaterialDto>{

    String name;

    String description;

    String notes;

    UUID categoryId;
}
