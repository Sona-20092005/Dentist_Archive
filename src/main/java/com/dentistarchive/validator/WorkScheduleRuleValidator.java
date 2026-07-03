package com.dentistarchive.validator;

import com.dentistarchive.entity.schedule.WorkSchedule;
import com.dentistarchive.entity.schedule.WorkScheduleRule;
import com.dentistarchive.exception.EntityNotFoundByIdException;
import com.dentistarchive.exception.InvalidWorkSchedulePeriodException;
import com.dentistarchive.exception.WorkScheduleRuleOverlapException;
import com.dentistarchive.repository.WorkScheduleRepository;
import com.dentistarchive.repository.WorkScheduleRuleRepository;
import com.dentistarchive.utils.ScheduleUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static com.dentistarchive.utils.ScheduleUtils.timesOverlap;

@Component
public class WorkScheduleRuleValidator {
    // TODO: 7/2/2026 exception mechanism
    // TODO: 7/2/2026 repo find methods archived checks

    private final WorkScheduleRepository workScheduleRepository;
    private final WorkScheduleRuleRepository workScheduleRuleRepository;


    public WorkScheduleRuleValidator(WorkScheduleRepository workScheduleRepository,
                                     WorkScheduleRuleRepository workScheduleRuleRepository) {
        this.workScheduleRepository = workScheduleRepository;
        this.workScheduleRuleRepository = workScheduleRuleRepository;
    }

    public void validate(WorkScheduleRule rule) {

        validateTimeRange(rule);

        WorkSchedule schedule = workScheduleRepository.getByIdAndNotArchived(rule.getScheduleId())
                        .orElseThrow(() ->  new EntityNotFoundByIdException(WorkSchedule.class, rule.getScheduleId()));

        validateNoConflicts(rule, schedule);
    }

    public void validateRevision(List<WorkScheduleRule> rules, WorkSchedule schedule) {
        for (WorkScheduleRule rule : rules) {
            validateTimeRange(rule);
        }

        List<WorkSchedule> overlappingSchedules = findOverlappingSchedules(schedule);
        List<WorkScheduleRule> existingRules = findRules(overlappingSchedules);

        validateInternalConflicts(rules);

        for (WorkScheduleRule rule : rules) {
            validateExternalConflicts(rule, existingRules);
        }
    }

    public void validateUpdate(WorkSchedule schedule) {

        List<WorkSchedule> overlappingSchedules = findOverlappingSchedules(schedule);
        List<WorkScheduleRule> existingRules = findRules(overlappingSchedules);

        List<WorkScheduleRule> currentRules =
                workScheduleRuleRepository.findByScheduleId(schedule.getId());

        for (WorkScheduleRule rule : currentRules) {
            validateExternalConflicts(rule, existingRules);
        }
    }

    private void validateTimeRange(WorkScheduleRule rule) {

        if (!ScheduleUtils.isTimeRangeValid(rule.getStartTime(), rule.getEndTime())) {
            throw new InvalidWorkSchedulePeriodException();
        }
    }

    private void validateNoConflicts(WorkScheduleRule rule, WorkSchedule currentSchedule) {
        List<WorkSchedule> overlappingSchedules = findOverlappingSchedules(currentSchedule);

        List<WorkScheduleRule> existingRules = findRules(overlappingSchedules);

        validateExternalConflicts(rule, existingRules);
    }

    private List<WorkSchedule> findOverlappingSchedules(WorkSchedule currentSchedule) {
        List<WorkSchedule> overlappingSchedules = new ArrayList<>();

        for (WorkSchedule schedule : workScheduleRepository.findByDoctorId(currentSchedule.getDoctorId())) {

            if (ScheduleUtils.periodsOverlap(
                    schedule.getEffectiveFrom(),
                    schedule.getEffectiveUntil(),
                    currentSchedule.getEffectiveFrom(),
                    currentSchedule.getEffectiveUntil())) {

                overlappingSchedules.add(schedule);
            }
        }

        return overlappingSchedules;
    }

    private List<WorkScheduleRule> findRules(List<WorkSchedule> schedules) {
        if (schedules.isEmpty()) {
            return Collections.emptyList();
        }

        List<UUID> scheduleIds = new ArrayList<>();

        for (WorkSchedule schedule : schedules) {
            scheduleIds.add(schedule.getId());
        }

        return workScheduleRuleRepository.findByScheduleIdIn(scheduleIds);
    }

    private void validateExternalConflicts(WorkScheduleRule rule, List<WorkScheduleRule> existingRules) {
        for (WorkScheduleRule existingRule : existingRules) {

            if (isSameRule(rule, existingRule)) {
                continue;
            }
            if (!isSameDay(rule, existingRule)) {
                continue;
            }
            if (timesOverlap(
                    existingRule.getStartTime(),
                    existingRule.getEndTime(),
                    rule.getStartTime(),
                    rule.getEndTime()
            )) {
                throw new WorkScheduleRuleOverlapException();
            }
        }
    }

    private void validateInternalConflicts(List<WorkScheduleRule> rules) {
        for (int i = 0; i < rules.size(); i++) {

            WorkScheduleRule first = rules.get(i);

            for (int j = i + 1; j < rules.size(); j++) {

                WorkScheduleRule second = rules.get(j);

                if (!isSameDay(first, second)) {
                    continue;
                }

                if (timesOverlap(
                        first.getStartTime(),
                        first.getEndTime(),
                        second.getStartTime(),
                        second.getEndTime())) {

                    throw new WorkScheduleRuleOverlapException();
                }
            }
        }
    }


    private boolean isSameDay(WorkScheduleRule first, WorkScheduleRule second) {
        return first.getDayOfWeek() == second.getDayOfWeek();
    }

    private boolean isSameRule(WorkScheduleRule first, WorkScheduleRule second) {
        return first.getId() != null && first.getId().equals(second.getId());
    }
}