package com.dentistarchive.utils;

import com.dentistarchive.exception.InvalidToothNumberException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.regex.Pattern;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ToothNumberValidator {
    private static final Pattern TOOTH_NUMBER_PATTERN =
            Pattern.compile("^[1-4][1-8]$");

    public static void assertThatToothNumberIsValid(int toothNumber) {
        if (!TOOTH_NUMBER_PATTERN.matcher(String.valueOf(toothNumber)).matches()) {
            throw new InvalidToothNumberException();
        }
    }
}
