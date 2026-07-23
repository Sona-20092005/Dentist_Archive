package com.dentistarchive.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ScheduleUtils {

    public static boolean isPeriodValid(LocalDate start, LocalDate end) {
        return end == null || !start.isAfter(end);
    }

    public static boolean isTimeRangeValid(LocalTime start, LocalTime end) {
        return start.isBefore(end);
    }

    public static boolean periodsOverlap(LocalDate start1, LocalDate end1,
                                         LocalDate start2, LocalDate end2) {

        LocalDate actualEnd1 =
                end1 != null ? end1 : LocalDate.MAX;

        LocalDate actualEnd2 =
                end2 != null ? end2 : LocalDate.MAX;

        return !actualEnd1.isBefore(start2)
                && !actualEnd2.isBefore(start1);
    }

    public static boolean timesOverlap(LocalTime start1, LocalTime end1,
                                       LocalTime start2, LocalTime end2) {
        return start1.isBefore(end2) && start2.isBefore(end1);
    }

    public static boolean isDateWithinPeriod(LocalDate date, LocalDate start, LocalDate end) {
        return !date.isBefore(start) && (end == null || !date.isAfter(end));
    }

    public static boolean isTimeWithinRange(LocalTime time, LocalTime start, LocalTime end) {
        return !time.isBefore(start) && !time.isAfter(end);
    }

    public static boolean areAdjacent(LocalTime firstEnd, LocalTime secondStart) {
        return firstEnd.equals(secondStart);
    }

}
