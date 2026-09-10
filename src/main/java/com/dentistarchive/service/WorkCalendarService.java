package com.dentistarchive.service;

import com.dentistarchive.dto.WorkSessionDto;
import com.dentistarchive.entity.schedule.WorkSchedule;
import com.dentistarchive.entity.schedule.WorkScheduleModification;
import com.dentistarchive.entity.schedule.WorkScheduleRule;
import com.dentistarchive.exception.EntityNotFoundByIdException;
import com.dentistarchive.mapper.WorkSessionMapper;
import com.dentistarchive.repository.WorkScheduleModificationRepository;
import com.dentistarchive.repository.WorkScheduleRepository;
import com.dentistarchive.repository.WorkScheduleRuleRepository;
import com.dentistarchive.utils.ScheduleUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import static com.dentistarchive.utils.ScheduleUtils.areAdjacent;
import static com.dentistarchive.utils.ScheduleUtils.isTimeWithinRange;

@Service
@Validated
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class WorkCalendarService {
    WorkScheduleRepository workScheduleRepository;
    WorkScheduleRuleRepository workScheduleRuleRepository;
    WorkScheduleModificationRepository workScheduleModificationRepository;
    WorkSessionMapper workSessionMapper;

    public List<WorkSessionDto> getScheduleSessions(UUID scheduleId, LocalDate date) {
        return getScheduleSessions(scheduleId, date, date);
    }

    public List<WorkSessionDto> getScheduleSessions(WorkSchedule schedule, LocalDate date) {
        return getScheduleSessions(schedule, date, date);
    }

    public List<WorkSessionDto> getScheduleSessions(UUID scheduleId, LocalDate from, LocalDate until) {

        WorkSchedule schedule = workScheduleRepository.getByIdAndNotArchived(scheduleId).orElseThrow(() ->
                new EntityNotFoundByIdException(WorkSchedule.class, scheduleId));

        return from.datesUntil(until.plusDays(1))
                .flatMap(date -> buildSchedule(schedule, date).stream())
                .toList();
    }

    public List<WorkSessionDto> getScheduleSessions(WorkSchedule schedule, LocalDate from, LocalDate until) {

        return from.datesUntil(until.plusDays(1))
                .flatMap(date -> buildSchedule(schedule, date).stream())
                .toList();
    }

    public List<WorkSessionDto> getDoctorSessions(UUID doctorId, LocalDate date) {
        return getDoctorSessions(doctorId, date, date);
    }

    public List<WorkSessionDto> getDoctorSessions(UUID doctorId, LocalDate from, LocalDate until) {

        List<WorkSessionDto> sessions = new ArrayList<>();

        List<WorkSchedule> schedules = workScheduleRepository.findEffectiveSchedules(doctorId, from, until);

        for (WorkSchedule schedule : schedules) {
            sessions.addAll(getScheduleSessions(schedule, from, until));
        }

        return sessions;
    }

    public Optional<WorkSessionDto> findRuleSession(UUID scheduleId, LocalDate date, LocalTime startTime, LocalTime endTime) {
        return getScheduleSessions(scheduleId, date).stream()
                .filter(session ->
                        session.getModificationId() == null
                                && session.getDate().equals(date)
                                && session.getStartTime().equals(startTime)
                                && session.getEndTime().equals(endTime))
                .findFirst();
    }

    public boolean hasOverlappingSession(UUID doctorId, LocalDate date, LocalTime startTime, LocalTime endTime) {
        return getDoctorSessions(doctorId, date).stream()
                .anyMatch(session ->
                        ScheduleUtils.timesOverlap(
                                startTime,
                                endTime,
                                session.getStartTime(),
                                session.getEndTime()));
    }

    public boolean hasOverlappingSessionExceptModification(UUID doctorId, LocalDate date, LocalTime startTime, LocalTime endTime, UUID ignoredModificationId) {
        return getDoctorSessions(doctorId, date).stream()
                .filter(session -> !ignoredModificationId.equals(session.getModificationId()))
                .anyMatch(session ->
                        ScheduleUtils.timesOverlap(
                                startTime,
                                endTime,
                                session.getStartTime(),
                                session.getEndTime()));
    }

    public boolean hasOverlappingSession(UUID doctorId, LocalDate date, LocalTime startTime,
            LocalTime endTime, LocalDate ignoredSourceDate,
            LocalTime ignoredSourceStartTime, LocalTime ignoredSourceEndTime) {

        return getDoctorSessions(doctorId, date).stream()
                .filter(session ->
                        !session.getDate().equals(ignoredSourceDate)
                                || !session.getStartTime().equals(ignoredSourceStartTime)
                                || !session.getEndTime().equals(ignoredSourceEndTime))
                .anyMatch(session ->
                        ScheduleUtils.timesOverlap(
                                startTime,
                                endTime,
                                session.getStartTime(),
                                session.getEndTime()));
    }

    // TODO: 7/23/2026 write a better algorithm
    public boolean isAvailable(UUID scheduleId, LocalDate date, LocalTime startTime, LocalTime endTime) {
        List<WorkSessionDto> sessions = getScheduleSessions(scheduleId, date).stream()
                .sorted(Comparator.comparing(WorkSessionDto::getStartTime))
                .toList();

        boolean started = false;

        for (int i = 0; i < sessions.size(); i++) {

            WorkSessionDto current = sessions.get(i);

            if (!started) {
                if (!isTimeWithinRange(startTime, current.getStartTime(), current.getEndTime())) {
                    continue;
                }
                started = true;
            }

            if (isTimeWithinRange(endTime, current.getStartTime(), current.getEndTime())) {
                return true;
            }

            if (i == sessions.size() - 1) {
                return false;
            }

            WorkSessionDto next = sessions.get(i + 1);

            if (!areAdjacent(current.getEndTime(), next.getStartTime())) {
                return false;
            }
        }

        return false;
    }

    private List<WorkSessionDto> buildSchedule(WorkSchedule schedule, LocalDate date) {

        if (date.isBefore(schedule.getEffectiveFrom()) || (schedule.getEffectiveUntil() != null
                && date.isAfter(schedule.getEffectiveUntil()))) {
            return List.of();
        }

        List<WorkSessionDto> sessions = new ArrayList<>();

        addRules(schedule, date, sessions);
        addModifications(schedule, date, sessions);

        return sessions;
    }

    private void addModifications(WorkSchedule schedule, LocalDate date, List<WorkSessionDto> sessions) {
        List<WorkScheduleModification> modifications = workScheduleModificationRepository.findEffectiveModifications(schedule.getId(), date);

        for (WorkScheduleModification modification : modifications) {
            switch (modification.getModificationType()) {
                case ADD -> sessions.add(workSessionMapper.toWorkSession(modification));
                case MODIFY -> {
                    if (date.equals(modification.getSourceDate())) {
                        if (!removeSessions(modification, sessions)) {
                            throw new IllegalStateException("Could not find source session for modification.");
                        }
                    }
                    if (date.equals(modification.getDate())) {
                        sessions.add(workSessionMapper.toWorkSession(modification));
                    }
                }
                case CANCEL -> {
                    if (!removeSessions(modification, sessions)) {
                        throw new IllegalStateException("Could not find source session for cancellation.");
                    }
                }
            }
        }
    }

    private void addRules(WorkSchedule schedule, LocalDate date, List<WorkSessionDto> sessions) {
        List<WorkScheduleRule> rules = workScheduleRuleRepository.findByScheduleIdAndDayOfWeek(
                        schedule.getId(),
                        date.getDayOfWeek());

        for (WorkScheduleRule rule : rules) {
            sessions.add(workSessionMapper.toWorkSession(rule, date));
        }
    }

    private boolean removeSessions(WorkScheduleModification modification, List<WorkSessionDto> sessions) {
        return sessions.removeIf(session -> session.getDate().equals(modification.getSourceDate())
                && session.getStartTime().equals(modification.getSourceStartTime())
                && session.getEndTime().equals(modification.getSourceEndTime())
                && session.getModificationId() == null);

    }

}
