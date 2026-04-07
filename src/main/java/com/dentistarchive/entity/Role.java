package com.dentistarchive.entity;

import com.dentistarchive.dto.auth.enums.UserScope;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.FieldNameConstants;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Type;

import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@FieldNameConstants
public class Role extends BaseEntity {

    @Column(nullable = false)
    String name;

    @Column(nullable = false, unique = true)
    String code;

    String description;

    boolean clientRole;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    UserScope scope;

    @Type(JsonType.class)
    @Builder.Default
    Set<String> permissionCodes = new HashSet<>();

}
