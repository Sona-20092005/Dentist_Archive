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

import java.util.*;

@Data
@Entity
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@FieldNameConstants
public class Role extends BaseEntity {

    // TODO: 4/7/2026 adding new HashMap is a temporary solution, needs changing
    @NonNull
    @Column(nullable = false)
    @Type(JsonType.class)
    @Builder.Default
    Map<Locale, String> namesMap = new HashMap<>();

    @NonNull
    @Column(nullable = false, unique = true)
    String code;

    @NonNull
    @Column(nullable = false)
    @Type(JsonType.class)
    @Builder.Default
    Map<Locale, String> descriptionsMap = new HashMap<>();

    boolean clientRole;

    @NonNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    UserScope scope;

    @NonNull
    @Type(JsonType.class)
    @Builder.Default
    Set<String> permissionCodes = new HashSet<>();

    public String getName(Locale locale) {
        return namesMap.get(locale);
    }

    public String getDescription(Locale locale) {
        return descriptionsMap.get(locale);
    }
}
