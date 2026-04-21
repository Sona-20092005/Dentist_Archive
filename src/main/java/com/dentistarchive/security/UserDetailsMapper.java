package com.dentistarchive.security;

import com.dentistarchive.entity.User;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDetailsMapper {

    public CustomUserDetails toPrincipal(User user) {
        return toPrincipal(user, !user.isArchived());
    }

    public CustomUserDetails toPrincipal(User user, boolean isAuthenticated) {
        if (user == null) {
            return null;
        }

        return new CustomUserDetails(
                user.getId(),
                user.getUserName(),
                user.getPasswordHash(),
                user.getRole(),
                isAuthenticated
        );
    }
}