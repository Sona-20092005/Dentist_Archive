package com.dentistarchive.dto.update;

import com.dentistarchive.dto.TechnicianDto;
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

    String name;

    List<String> phones;

    List<String> emails;

    String notes;

    LocalDate collaborationStartDate;

    LocalDate collaborationEndDate;

    LocalDate monthlyReportStartDate;

    String address;
}
