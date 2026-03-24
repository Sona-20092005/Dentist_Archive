package com.dentistarchive.utils;

import lombok.Setter;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

public class ClockUtils {

    @Setter
    private static Clock clock = Clock.tickMillis(ZoneOffset.UTC);

    public static OffsetDateTime now() {
        return OffsetDateTime.now(clock);
    }

    public static LocalDateTime nowInTimeZone(ZoneOffset offset) {
        return now().withOffsetSameInstant(offset).toLocalDateTime();
    }

    public static LocalDateTime nowInUTC() {
        return nowInTimeZone(ZoneOffset.UTC);
    }

    public static LocalDate todayInTimeZone(ZoneOffset offset) {
        return nowInTimeZone(offset).toLocalDate();
    }

    public static LocalDate todayInUTC() {
        return todayInTimeZone(ZoneOffset.UTC);
    }
}
