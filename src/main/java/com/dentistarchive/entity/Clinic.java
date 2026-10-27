package com.dentistarchive.entity;

import com.dentistarchive.enums.PaymentMethod;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Type;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Clinic extends ArchivableBaseEntity {

    @NotBlank
    @Column(nullable = false)
    String name;

    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb")
    List<@NotBlank String> phones;

    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb")
    List<@NotBlank @Email String> emails;

    String address;

    @DecimalMin("0.00")
    @DecimalMax("100.00")
    @Column(name = "commission_percent", nullable = false)
    BigDecimal commissionPercent;

    @Type(JsonType.class)
    @Column(name = "commission_payment_methods", columnDefinition = "jsonb")
    List<PaymentMethod> commissionPaymentMethods;

    String notes;

    @NotNull
    @Column(name = "doctor_id", nullable = false)
    UUID doctorId;

}


