package com.dentistarchive.entity;

import com.dentistarchive.dto.auth.enums.UserScope;
import com.dentistarchive.utils.ClockUtils;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Type;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Data
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "user_type", discriminatorType = DiscriminatorType.STRING)
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PROTECTED)
public abstract class User extends ArchivingBaseEntity {

    @Column(nullable = false, unique = true)
    String nickName;

    @Column(nullable = false)
    String fullName;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    UserScope scope;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    Set<Role> roles;

    @Column(nullable = false)
    String passwordHash;

    @Column(nullable = false)
    OffsetDateTime passwordSetAt;

    boolean temporaryPassword;

    @Setter(AccessLevel.NONE)
    int numberOfFailedLoginAttempts;

    @Setter(AccessLevel.NONE)
    OffsetDateTime lastLoginFailedAt;

    @Type(JsonType.class)
    @Builder.Default
    Map<String, String> additionalParams = new HashMap<>();

    @Override
    protected void prePersist() {
        super.prePersist();
        passwordSetAt = getCreatedAt();
        if (nickName == null) {
            nickName = id.toString();
        }
    }

    public boolean hasFailedLoginAttempts() {
        return numberOfFailedLoginAttempts > 0;
    }

    public void incrementNumberOfFailedLoginAttempts() {
        numberOfFailedLoginAttempts += 1;
        lastLoginFailedAt = ClockUtils.now();
    }

    public void clearNumberOfFailedLoginAttempts() {
        numberOfFailedLoginAttempts = 0;
        lastLoginFailedAt = null;
    }
}

