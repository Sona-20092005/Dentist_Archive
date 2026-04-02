package com.dentistarchive.search.filter;

import com.dentistarchive.dto.auth.enums.UserScope;
import io.swagger.v3.oas.annotations.media.Schema;
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
public class UserFilter extends ArchivingBaseFilter<UserFilter> {

    @Schema(hidden = true)
    String login;

    @Singular
    @Schema(description = "Actors from one of given scopes")
    Set<@NotNull UserScope> scopes;

    public static UserFilter byId(UUID id) {
        return UserFilter.byIds(Set.of(id));
    }

    public static UserFilter byIds(Set<UUID> ids) {
        var filter = new UserFilter();
        filter.setIds(ids);
        return filter;
    }
}
