package com.dentistarchive.controller;

import com.dentistarchive.dto.WorkScheduleRuleDto;
import com.dentistarchive.dto.create.WorkScheduleRuleCreateDto;
import com.dentistarchive.dto.update.WorkScheduleRuleCorrectDto;
import com.dentistarchive.mapper.WorkScheduleRuleMapper;
import com.dentistarchive.search.filter.SearchRequest;
import com.dentistarchive.search.filter.SearchResponse;
import com.dentistarchive.search.filter.WorkScheduleRuleFilter;
import com.dentistarchive.search.sort.WorkScheduleRuleSort;
import com.dentistarchive.service.WorkScheduleRuleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@Tag(name = "Work Schedule Rule")
@RequestMapping("/api/v1/work-schedule-rules")
@PreAuthorize("hasRole('DOCTOR')")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WorkScheduleRuleController extends BaseController {

    WorkScheduleRuleService ruleService;
    WorkScheduleRuleMapper ruleMapper;

    @GetMapping("/{id}")
    @Operation(summary = "Get work schedule rule by id")
    public ResponseEntity<WorkScheduleRuleDto> getWorkScheduleById(@PathVariable UUID id) {
        return ResponseEntity.ok(ruleMapper.toDto(ruleService.getByIdOrElseThrow(id)));
    }

    @PostMapping("/search")
    @Operation(summary = "Search work schedule rule")
    public SearchResponse<WorkScheduleRuleDto> search(@RequestBody SearchRequest<WorkScheduleRuleFilter, WorkScheduleRuleSort> searchRequest) {
        return ruleMapper.toSearchResponse(ruleService.search(searchRequest));
    }

    @PostMapping
    @Operation(summary = "Create work schedule rule")
    public ResponseEntity<WorkScheduleRuleDto> create(@Valid @RequestBody WorkScheduleRuleCreateDto createDto) {
        return ResponseEntity.ok(ruleMapper.toDto(ruleService.create(createDto)));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Correct work schedule rule info")
    public ResponseEntity<WorkScheduleRuleDto> update(@PathVariable UUID id,
                                             @Valid @RequestBody WorkScheduleRuleCorrectDto correctDto) {
        return ResponseEntity.ok(ruleMapper.toDto(ruleService.correct(id, correctDto)));
    }


    @DeleteMapping("/{id}")
    @Operation(summary = "Delete (archive) work schedule rule")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        ruleService.archiveById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/restore")
    @Operation(summary = "Restore (unarchive) work schedule rule")
    public ResponseEntity<WorkScheduleRuleDto> unarchive(@PathVariable UUID id) {
        return ResponseEntity.ok(
                ruleMapper.toDto(ruleService.unarchiveById(id))
        );
    }

}
