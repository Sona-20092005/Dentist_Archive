package com.dentistarchive.search.filter;

import com.dentistarchive.dto.auth.enums.UserScope;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.Set;
import java.util.UUID;

// TODO: 4/2/2026 fix with our logic
@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DoctorFilter extends ArchivingBaseFilter<DoctorFilter> {

    String fullNameContains;

    @Singular
    Set<@NotBlank @Email String> emails;

//    @Singular
//    Set<@NotBlank @PhoneNumber String> phones;

    @Singular
    @Schema(description = "Users from one of given scopes")
    Set<@NotNull UserScope> scopes;

    @Singular
    Set<@NotNull UUID> companyIds;

    Boolean inCompany;

    public static DoctorFilter byId(UUID id) {
        return DoctorFilter.byIds(Set.of(id));
    }

    public static DoctorFilter byIds(Set<UUID> ids) {
        var filter = new DoctorFilter();
        filter.setIds(ids);
        return filter;
    }
}
