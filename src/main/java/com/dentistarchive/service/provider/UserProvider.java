package com.dentistarchive.service.provider;

import com.dentistarchive.dto.ChangePasswordCommand;
import com.dentistarchive.entity.User;
import com.dentistarchive.exception.CustomValidationException;
import com.dentistarchive.exception.ErrorCode;
import com.dentistarchive.security.AuthUtils;
import com.dentistarchive.utils.ClockUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import static com.dentistarchive.utils.PasswordValidator.assertThatPasswordIsValid;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserProvider {

    PasswordEncoder passwordEncoder;

    public void changePassword(User user, ChangePasswordCommand command) {
        if (command.getOldPassword() == null
                || !passwordEncoder.matches(command.getOldPassword(), user.getPasswordHash())) {
            throw AuthUtils.getUnauthorizedException();
        }
        if (passwordEncoder.matches(command.getNewPassword(), user.getPasswordHash())) {
            throw new CustomValidationException(ChangePasswordCommand.Fields.newPassword,
                    ErrorCode.USER_PASSWORD_SAME_AS_OLD);
        }
        setPassword(user, command.getNewPassword());
    }

    private void setPassword(User user, String password) {
        assertThatPasswordIsValid(user, password);

        user.setPasswordHash(passwordEncoder.encode(password));

        var now = ClockUtils.now();
        user.setPasswordSetAt(now);
    }
}
