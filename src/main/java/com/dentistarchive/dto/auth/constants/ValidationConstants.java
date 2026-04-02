package com.dentistarchive.dto.auth.constants;

public interface ValidationConstants {
    String NICKNAME_PATTERN = "[A-Za-z0-9_\\-.]{3,50}";
    String NICKNAME_PATTERN_ERROR_CODE = "{validation.actor.nickName.pattern}";

    String FULL_NAME_PATTERN = "[A-Za-zА-Яа-я\\s\\-'ёЁўЎғҒҳҲқҚ]+";
    String FULL_NAME_PATTERN_ERROR_CODE = "{validation.actor.fullName.pattern}";

    int UZ_PHONE_CODE = 998;
    int UZ_PHONE_DIGITS = 12;
}
