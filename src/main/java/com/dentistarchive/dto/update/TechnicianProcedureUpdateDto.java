package com.dentistarchive.dto.update;

import com.dentistarchive.dto.TechnicianProcedureDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
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
public class TechnicianProcedureUpdateDto extends BaseUpdateDto<TechnicianProcedureDto> {

    @NotBlank
    String name;

    @NotNull
    @PositiveOrZero
    BigDecimal price;

    String description;

    @NotNull
    UUID technicianId;
}
