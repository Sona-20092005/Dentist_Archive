package com.dentistarchive.dto.create;

import com.dentistarchive.dto.NurseCompensationTypeChangeDto;
import com.dentistarchive.enums.CompensationType;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.FieldNameConstants;
import lombok.experimental.SuperBuilder;

import java.time.YearMonth;
import java.util.UUID;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@FieldNameConstants
public class NurseCompensationTypeChangeCreateDto extends BaseCreateDto<NurseCompensationTypeChangeDto>{

    UUID nurseId;

    CompensationType compensationType;

    YearMonth effectiveFrom;
}
