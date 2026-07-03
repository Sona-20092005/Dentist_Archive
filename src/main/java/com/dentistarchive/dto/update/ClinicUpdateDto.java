package com.dentistarchive.dto.update;

import com.dentistarchive.dto.ClinicDto;
import com.dentistarchive.dto.create.BaseCreateDto;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.FieldNameConstants;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@FieldNameConstants
public class ClinicUpdateDto extends BaseCreateDto<ClinicDto> {

    String name;

    List<String> phones;

    List<String> emails;

    String address;

    String notes;
}
