package com.dentistarchive.dto.update;

import com.dentistarchive.dto.NurseCompensationTypeChangeDto;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.FieldNameConstants;
import lombok.experimental.SuperBuilder;

import java.time.YearMonth;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@FieldNameConstants
public class NurseCompensationTypeChangeUpdateDto extends BaseUpdateDto<NurseCompensationTypeChangeDto> {

    YearMonth effectiveFrom;

}
