package com.dentistarchive.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PasswordGenerator {

    // at least 1 digit
    private static final String PASSWORD_ALPHABET_1 = "0123456789";
    // at least 1 lowercase letter
    private static final String PASSWORD_ALPHABET_2 = "abcdefghijklmnopqrstuvwxyz";
    // at least 1 uppercase letter
    private static final String PASSWORD_ALPHABET_3 = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    // at least 1 special character
    private static final String PASSWORD_ALPHABET_4 = "-_!@#$%^&*()";

    private static final Set<String> PASSWORD_ALPHABETS = Set.of(
            PASSWORD_ALPHABET_1,
            PASSWORD_ALPHABET_2,
            PASSWORD_ALPHABET_3,
            PASSWORD_ALPHABET_4
    );
    private static final String ALLOWED_CHARACTERS = String.join("", PASSWORD_ALPHABETS);

    private static final int DEFAULT_PASSWORD_LENGTH = 15;

    public static String generatePassword(int length) {
        if (length < PASSWORD_ALPHABETS.size()) {
            throw new IllegalArgumentException("Password length can't be less than " + PASSWORD_ALPHABETS.size());
        }

        // take 1 random char from each alphabet
        List<Character> chars = PASSWORD_ALPHABETS.stream()
                .map(alphabet -> RandomStringUtils.random(1, alphabet))
                .flatMap(part -> part.chars().mapToObj(c -> (char) c))
                .collect(toList());

        // add other random chars to reach specified password length
        RandomStringUtils.random(length - chars.size(), ALLOWED_CHARACTERS).chars()
                .mapToObj(c -> (char) c)
                .forEach(chars::add);

        // shuffle all taken chars
        Collections.shuffle(chars);

        return chars.stream().map(Object::toString).collect(joining(""));
    }

    public static String generatePassword() {
        return generatePassword(DEFAULT_PASSWORD_LENGTH);
    }
}
