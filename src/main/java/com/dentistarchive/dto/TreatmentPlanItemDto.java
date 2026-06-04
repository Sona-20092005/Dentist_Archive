package com.dentistarchive.dto;

import com.dentistarchive.enums.TreatmentPlanItemStatus;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PROTECTED)
public class TreatmentPlanItemDto extends ArchivingBaseDto {

    TreatmentPlanItemStatus itemStatus;

    BigDecimal unitPrice;

    List<Integer> toothNumbers;

    String notes;

    UUID procedureId;

    UUID treatmentPlanId;
}
