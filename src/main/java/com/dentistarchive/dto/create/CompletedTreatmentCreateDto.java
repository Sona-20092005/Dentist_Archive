package com.dentistarchive.dto.create;

import com.dentistarchive.dto.CompletedTreatmentDto;
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
public class CompletedTreatmentCreateDto extends BaseCreateDto<CompletedTreatmentDto>{

    LocalDate date;

    BigDecimal unitPrice;

    List<Integer> toothNumbers;

    String notes;

    UUID procedureId;

    UUID appointmentId;

    UUID treatmentPlanItemId;

    UUID patientId;

}
