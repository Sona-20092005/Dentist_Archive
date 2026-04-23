package com.dentistarchive.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum ErrorCode {

    SERVER_ERROR("server.error"),
    VALIDATION_ERROR("validation.error"),
    ENTITIES_NOT_FOUND("validation.entities-not-found"),
    ENTITY_NOT_FOUND("validation.entity-not-found"),
    IMPOSSIBLE_TO_PATCH_ARCHIVED_ENTITY("validation.impossible-to-patch-archived-entity"),
    FILE_MUST_NOT_BE_EMPTY("validation.file-must-not-be-empty"),
    USER_PASSWORD_IS_NOT_SECURE("validation.user.password.not-secure"),
    USER_PASSWORD_SAME_AS_OLD("validation.user.password.same-as-old"),
    LOGIN_DELAY_AFTER_FAILED_ATTEMPTS("validation.login-delay-after-failed-attempts"),
    USER_NOT_FOUND("validation.user-not-found"),
    AUTHORIZATION_BY_TEMPORARY_PASSWORD("validation.authorization.by-temporary-password"),
    REFRESH_TOKEN_INVALIDATED("validation.refresh-token-invalidated"),
    ACCESS_TOKEN_FORMAT_INVALID("access-token.format.invalid"),
    ACCESS_TOKEN_SIGNATURE_INVALID("access-token.signature.invalid"),
    ACCESS_TOKEN_EXPIRED("access-token.expired");

    String code;
}
