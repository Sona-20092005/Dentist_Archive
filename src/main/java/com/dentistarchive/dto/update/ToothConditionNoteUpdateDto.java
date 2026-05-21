package com.dentistarchive.dto.update;

import com.dentistarchive.dto.ToothConditionNoteDto;
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
public class ToothConditionNoteUpdateDto extends BaseUpdateDto<ToothConditionNoteDto> {
    String description;
}
