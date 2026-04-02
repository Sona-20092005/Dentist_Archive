package com.dentistarchive.utils;

import com.dentistarchive.entity.Doctor;
import com.dentistarchive.entity.User;
import com.dentistarchive.exception.actor.PasswordIsNotSecureException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PasswordValidator {

    public static final int USER_PASSWORD_MIN_LENGTH = 5;
    public static final int CLIENT_PASSWORD_MIN_LENGTH = 15;
    public static final int PASSWORD_MAX_LENGTH = 50;

    // only ASCII (latin letters, digits, punctuation)
    private static final Pattern PASSWORD_ALPHABET_0 = Pattern.compile("^[\\x00-\\x7F]+$");
    // at least 1 digit
    private static final Pattern PASSWORD_ALPHABET_1 = Pattern.compile(".*?\\d+.*?");
    // at least 1 lowercase letter
    private static final Pattern PASSWORD_ALPHABET_2 = Pattern.compile(".*?[a-z]+.*?");
    // at least 1 uppercase letter
    private static final Pattern PASSWORD_ALPHABET_3 = Pattern.compile(".*?[A-Z]+.*?");
    // at least 1 special character
    private static final Pattern PASSWORD_ALPHABET_4 = Pattern.compile(".*?[-_!@#$%^&*()]+.*?");
    // no spaces
    private static final Pattern PASSWORD_ALPHABET_5 = Pattern.compile("^\\S+$");
    public static final List<Pattern> PASSWORDS_ALPHABETS = List.of(
            PASSWORD_ALPHABET_0,
            PASSWORD_ALPHABET_1,
            PASSWORD_ALPHABET_2,
            PASSWORD_ALPHABET_3,
            PASSWORD_ALPHABET_4,
            PASSWORD_ALPHABET_5
    );

    public static void assertThatPasswordIsValid(User user, String password) {
        if (user instanceof Doctor &&
                (password.length() < USER_PASSWORD_MIN_LENGTH || password.length() > PASSWORD_MAX_LENGTH)) {
            throw new PasswordIsNotSecureException(false);
        }
        long validPatterns = PASSWORDS_ALPHABETS
                .stream()
                .map(pattern -> pattern.matcher(password))
                .filter(Matcher::matches)
                .count();
        if (validPatterns < PASSWORDS_ALPHABETS.size()) {
            throw new PasswordIsNotSecureException(user instanceof Doctor);
        }
        if (user instanceof Doctor doctor &&
                (password.equals(doctor.getEmail()) || password.equals(doctor.getNickName()) || password.equals(user.getPhone()))) {
            throw new PasswordIsNotSecureException(false);
        }
    }
}
