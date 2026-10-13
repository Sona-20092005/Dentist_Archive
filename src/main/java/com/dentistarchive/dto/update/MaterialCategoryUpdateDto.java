package com.dentistarchive.dto.update;

import com.dentistarchive.dto.MaterialCategoryDto;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.FieldNameConstants;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@FieldNameConstants
public class MaterialCategoryUpdateDto extends BaseUpdateDto<MaterialCategoryDto> {

    String name;

    String description;
}
