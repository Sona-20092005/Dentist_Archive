package com.dentistarchive.dto.create;

import com.dentistarchive.dto.TechnicianWorkLogDto;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.FieldNameConstants;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@FieldNameConstants
public class TechnicianWorkLogCreateDto extends BaseCreateDto<TechnicianWorkLogDto>{

    @NotNull
    UUID technicianId;

    @NotNull
    LocalDate requestedDate;

    LocalDate completedDate;

    @PositiveOrZero
    BigDecimal unitPrice;

    @Positive
    Integer quantity;

    List<@NotNull Integer> toothNumbers;

    @NotNull
    UUID technicianProcedureId;

    @NotNull
    UUID patientId;

    @NotNull
    Boolean isPaid;

    LocalDate paymentDate;

    String notes;

}
