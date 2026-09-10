package com.dentistarchive.dto.create;

import com.dentistarchive.dto.TechnicianProcedureDto;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.FieldNameConstants;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@FieldNameConstants
public class TechnicianProcedureCreateDto extends BaseCreateDto<TechnicianProcedureDto>{

    String name;

    BigDecimal price;

    String description;

    UUID technicianId;
}
