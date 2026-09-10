package com.dentistarchive.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PROTECTED)
public class TechnicianWorkLogDto extends ArchivingBaseDto {

    UUID technicianId;

    LocalDate requestedDate;

    LocalDate completedDate;

    BigDecimal unitPrice;

    Integer quantity;

    List<Integer> toothNumbers;

    UUID technicianProcedureId;

    UUID patientId;

    Boolean isPaid;

    LocalDate paymentDate;

    String notes;
}
