//package com.dentistarchive.service;
//
//import com.dentistarchive.entity.schedule.WorkSchedule;
//import com.dentistarchive.entity.schedule.WorkScheduleModification;
//import com.dentistarchive.entity.schedule.WorkScheduleRule;
//import com.dentistarchive.dto.WorkSessionDto;
//import com.dentistarchive.enums.WorkSessionType;
//import com.dentistarchive.repository.WorkScheduleModificationRepository;
//import com.dentistarchive.repository.WorkScheduleRepository;
//import com.dentistarchive.repository.WorkScheduleRuleRepository;
//import lombok.AccessLevel;
//import lombok.experimental.FieldDefaults;
//import org.springframework.stereotype.Service;
//import org.springframework.validation.annotation.Validated;
//
//import java.time.LocalDate;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.UUID;
//
//@Service
//@Validated
//@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
//public class WorkCalendarService {
//    WorkScheduleRepository workScheduleRepository;
//    WorkScheduleRuleRepository workScheduleRuleRepository;
//    WorkScheduleModificationRepository workScheduleModificationRepository;
//
//    public WorkCalendarService(
//            WorkScheduleRepository workScheduleRepository,
//            WorkScheduleRuleRepository workScheduleRuleRepository,
//            WorkScheduleModificationRepository workScheduleModificationRepository
//    ) {
//        this.workScheduleRepository = workScheduleRepository;
//        this.workScheduleRuleRepository = workScheduleRuleRepository;
//        this.workScheduleModificationRepository = workScheduleModificationRepository;
//    }
//
//    List<WorkSessionDto> getScheduleSessions(UUID scheduleId, LocalDate date) {
//        return getScheduleSessions(scheduleId, date, date);
//    }
//
//    List<WorkSessionDto> getScheduleSessions(UUID scheduleId, LocalDate from, LocalDate until) {
//
//    }
//
//    List<WorkSessionDto> getDoctorSessions(UUID doctorId, LocalDate date) {
//        return getDoctorSessions(doctorId, date, date);
//    }
//
//    List<WorkSessionDto> getDoctorSessions(UUID doctorId, LocalDate from, LocalDate until) {
//
//    }
//
//    List<WorkSessionDto> buildSchedule(WorkSchedule schedule, LocalDate date) {
//        List<WorkScheduleRule> rules = workScheduleRuleRepository.findByScheduleIdAndDayOfWeek(
//                        schedule.getId(),
//                        date.getDayOfWeek());
//
//        List<WorkSessionDto> sessions = new ArrayList<>();
//
//        for (WorkScheduleRule rule : rules) {
//            sessions.add(toWorkSession(rule, date));
//        }
//
//        List<WorkScheduleModification> modifications = workScheduleModificationRepository.findByScheduleIdAndDate(schedule.getId(), date);
//
//
//
//        return sessions;
//    }
//
//    private WorkSessionDto toWorkSession(WorkScheduleRule rule, LocalDate date) {
//
//        return WorkSessionDto.builder()
//                .date(date)
//                .startTime(rule.getStartTime())
//                .endTime(rule.getEndTime())
//                .workSessionType(WorkSessionType.REGULAR)
//                .scheduleId(rule.getScheduleId())
//                .build();
//    }
//
//
//}
