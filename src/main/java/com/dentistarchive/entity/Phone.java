package com.dentistarchive.entity;

import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Phone extends MutableBaseEntity{

    @NonNull
    public String phone;

    //one to many
    @NonNull
//    @ColumnInfo(name = "patient_id")
    public UUID patientId;


}


