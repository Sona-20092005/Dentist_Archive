package com.dentistarchive.dto.create;

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
public class ToothConditionNoteCreateDto extends BaseCreateDto<ToothConditionNoteDto>{

    int number;

    String description;

}
