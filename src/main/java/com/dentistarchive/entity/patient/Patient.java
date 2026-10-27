package com.dentistarchive.entity.patient;

import com.dentistarchive.entity.ArchivableBaseEntity;
import com.dentistarchive.enums.PatientStatus;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Type;

import java.util.List;
import java.util.UUID;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Patient extends ArchivableBaseEntity {

    @NotBlank
    @Column(nullable = false)
    String name;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "patient_status", nullable = false)
    PatientStatus patientStatus;

    @NotNull
    @Column(name = "patient_status_sort_order", nullable = false)
    Integer patientStatusSortOrder;

    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb")
    List<@NotBlank String> phones;

    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb")
    List<@NotBlank @Email String> emails;

    String address;

    @Column(name = "passport_information")
    String passportInformation;

    String notes;

    @NotNull
    @Column(name = "doctor_id", nullable = false)
    UUID doctorId;
}


