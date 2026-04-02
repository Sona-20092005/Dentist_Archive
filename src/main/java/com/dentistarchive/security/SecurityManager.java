package com.dentistarchive.security;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Objects;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PROTECTED)
public class SecurityManager {

    private static final ThreadLocal<Boolean> ACCESS_CONTROL_ENABLED = ThreadLocal.withInitial(() -> true);

    final Supplier<UserDetails> clientDetailsSupplier;

    UserDetails clientDetails;

    public static void runAsActor(UserDetails userDetails, Runnable runnable) {
        UserDetails previousUserDetails = AuthHolder.getActorDetails().orElse(null);
        try {
            AuthHolder.setAuthentication(userDetails);
            runnable.run();
        } finally {
            AuthHolder.setAuthentication(previousUserDetails);
        }
    }

    public static <T> T runAsActor(UserDetails userDetails, Supplier<T> supplier) {
        UserDetails previousUserDetails = AuthHolder.getActorDetails().orElse(null);
        try {
            AuthHolder.setAuthentication(userDetails);
            return supplier.get();
        } finally {
            AuthHolder.setAuthentication(previousUserDetails);
        }
    }

    public static void runWithoutAuthentication(Runnable runnable) {
        UserDetails previousActorDetails = AuthHolder.getActorDetails().orElse(null);
        try {
            AuthHolder.clearAuthentication();
            runnable.run();
        } finally {
            AuthHolder.setAuthentication(previousActorDetails);
        }
    }

    public static <T> T runWithoutAuthentication(Supplier<T> supplier) {
        UserDetails previousActorDetails = AuthHolder.getActorDetails().orElse(null);
        try {
            AuthHolder.clearAuthentication();
            return supplier.get();
        } finally {
            AuthHolder.setAuthentication(previousActorDetails);
        }
    }

    public static boolean accessControlDisabled() {
        return !ACCESS_CONTROL_ENABLED.get();
    }

    public static boolean accessControlEnabled() {
        return ACCESS_CONTROL_ENABLED.get();
    }

    public static void runWithoutAccessControl(Runnable runnable) {
        try {
            ACCESS_CONTROL_ENABLED.set(false);
            runnable.run();
        } finally {
            ACCESS_CONTROL_ENABLED.set(true);
        }
    }

    public static <T> T runWithoutAccessControl(Supplier<T> supplier) {
        try {
            ACCESS_CONTROL_ENABLED.set(false);
            return supplier.get();
        } finally {
            ACCESS_CONTROL_ENABLED.set(true);
        }
    }

}
