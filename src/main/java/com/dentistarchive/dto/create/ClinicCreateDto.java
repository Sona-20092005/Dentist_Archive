package com.dentistarchive.dto.create;

import com.dentistarchive.dto.ProcedureDto;
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
public class ClinicCreateDto extends BaseCreateDto<ProcedureDto>{

    String name;

    List<String> phones;

    List<String> emails;

    String address;

    String notes;
}
