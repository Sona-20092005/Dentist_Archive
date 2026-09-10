package com.dentistarchive.entity.technician;

import com.dentistarchive.entity.ArchivableBaseEntity;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Type;

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
public class Technician extends ArchivableBaseEntity {

    @NotNull
    String name;

    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb")
    List<String> phones;

    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb")
    List<String> emails;

    String address;

    @Column(name = "collaboration_start_date")
    LocalDate collaborationStartDate;

    @Column(name = "collaboration_end_date")
    LocalDate collaborationEndDate;

    @NotNull
    @Column(name = "monthly_report_start_date")
    LocalDate monthlyReportStartDate;

    String notes;

    @NotNull
    @Column(name = "doctor_id", nullable = false)
    UUID doctorId;
}



