package com.dentistarchive.entity.technician;

import com.dentistarchive.entity.ArchivableBaseEntity;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Type;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TechnicianWorkLog extends ArchivableBaseEntity {

    @NonNull
    @Column(name = "technician_id", nullable = false)
    UUID technicianId;

    @NotNull
    @Column(name = "requested_date", nullable = false)
    LocalDate requestedDate;

    @Column(name = "completed_date")
    LocalDate completedDate;

    @Column(name = "unit_price")
    BigDecimal unitPrice;

    @Column(name = "quantity")
    Integer quantity;

    @Type(JsonType.class)
    @Column(name = "tooth_numbers", columnDefinition = "jsonb")
    List<Integer> toothNumbers;

    @NotNull
    @Column(name = "technician_procedure_id", nullable = false)
    UUID technicianProcedureId;

    @NonNull
    @Column(name = "patient_id", nullable = false)
    UUID patientId;

    @NonNull
    @Column(name = "is_paid", nullable = false)
    Boolean isPaid;

    @Column(name = "paymentDate")
    LocalDate paymentDate;

    String notes;

}



