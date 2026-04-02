package com.dentistarchive.exception.actor;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum ErrorCode {
    CAN_NOT_USE_ROLE_FROM_ANOTHER_SCOPE("validation.can-not-use-role-from-another-scope"),
    USER_CAN_NOT_HAVE_CLIENT_ROLE("validation.user-can-not-have-client-role"),
    CLIENT_CAN_NOT_HAVE_USER_ROLE("validation.client-can-not-have-user-role"),
    EMAIL_NOT_CONFIRMED("validation.email-not-confirmed"),
    USER_PASSWORD_IS_NOT_SECURE("validation.user.password.not-secure"),
    USER_PASSWORD_SAME_AS_OLD("validation.user.password.same-as-old"),
    CLIENT_PASSWORD_IS_NOT_SECURE("validation.client.password.not-secure"),
    LOGIN_DELAY_AFTER_FAILED_ATTEMPTS("validation.login-delay-after-failed-attempts"),
    USER_NOT_FOUND("validation.user-not-found"),
    ROLE_WITH_INCORRECT_SCOPE_SPECIFIED("validation.role-with-incorrect-scope-specified"),
    ;

    String code;
}
