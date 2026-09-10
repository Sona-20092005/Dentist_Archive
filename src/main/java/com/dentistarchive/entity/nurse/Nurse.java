package com.dentistarchive.entity.nurse;

import com.dentistarchive.entity.ArchivableBaseEntity;
import com.dentistarchive.enums.CompensationType;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
public class Nurse extends ArchivableBaseEntity {

    @NotNull
    String name;

    @Column(name = "base_salary")
    BigDecimal baseSalary;

    @Column(name = "hourly_rate")
    BigDecimal hourlyRate;

    @Column(name = "overtime_hourly_rate")
    BigDecimal overtimeHourlyRate;

    @Column(name = "contracted_weekly_hours")
    Integer contractedWeeklyHours;

    @NonNull
    @Enumerated(EnumType.STRING)
    @Column(name = "compensation_type")
    CompensationType compensationType;

    @Column(name = "hire_date")
    LocalDate hireDate;

    @Column(name = "termination_date")
    LocalDate terminationDate;

    @NotNull
    @Column(name = "payroll_start_date")
    LocalDate payrollStartDate;

    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb")
    List<String> phones;

    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb")
    List<String> emails;

    String notes;

    @NonNull
    @Column(name = "clinic_id", nullable = false)
    UUID clinicId;
}

// TODO: 9/4/2026 combine year and month into a single field


