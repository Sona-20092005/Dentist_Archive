package com.dentistarchive.security;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Optional;
import java.util.UUID;

/**
 * For now session has only "id" attribute without any additional meta information.
 * Session id is just for grouping requests, it may be useful for observing behavior of not authorized users.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SessionHolder {

    private static final ThreadLocal<UUID> SESSION_ID_THREAD_LOCAL = ThreadLocal.withInitial(() -> null);

    public static Optional<UUID> getSessionId() {
        return Optional.ofNullable(SESSION_ID_THREAD_LOCAL.get());
    }

    public static boolean hasSessionId() {
        return SESSION_ID_THREAD_LOCAL.get() != null;
    }

    public static void setSessionId(UUID sessionId) {
        SESSION_ID_THREAD_LOCAL.set(sessionId);
    }

    public static void removeSessionId() {
        SESSION_ID_THREAD_LOCAL.remove();
    }
}
