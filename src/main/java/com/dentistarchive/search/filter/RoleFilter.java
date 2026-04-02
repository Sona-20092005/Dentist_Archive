package com.dentistarchive.search.filter;

import com.dentistarchive.dto.auth.enums.UserScope;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.Set;

// TODO: 4/2/2026 fix this with our logic
@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleFilter extends BaseFilter<RoleFilter> {

    @Schema(description = "Roles available only for clients")
    Boolean clientRole;

    @Singular
    Set<@NotBlank String> codes;

    @Singular
    @Schema(description = "Roles that can be used in one of given scopes")
    Set<@NotNull UserScope> scopes;
}
