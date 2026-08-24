package com.dentistarchive.controller;

import com.dentistarchive.dto.NurseCompensationTypeChangeDto;
import com.dentistarchive.dto.create.NurseCompensationTypeChangeCreateDto;
import com.dentistarchive.dto.update.NurseCompensationTypeChangeUpdateDto;
import com.dentistarchive.mapper.NurseCompensationTypeChangeMapper;
import com.dentistarchive.service.NurseCompensationTypeChangeService;
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
@Tag(name = "Nurse Compensation Type Change")
@RequestMapping("/api/v1/nurse-compensation-type-changes")
@PreAuthorize("hasRole('DOCTOR')")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NurseCompensationTypeChangeController extends BaseController {

    NurseCompensationTypeChangeService nurseCompensationTypeChangeService;
    NurseCompensationTypeChangeMapper nurseCompensationTypeChangeMapper;

    @GetMapping("/{id}")
    @Operation(summary = "Get nurse compensation type change by id")
    public ResponseEntity<NurseCompensationTypeChangeDto> getNurseCompensationTypeChangeById(@PathVariable UUID id) {
        return ResponseEntity.ok(nurseCompensationTypeChangeMapper.toDto(nurseCompensationTypeChangeService.getByIdOrElseThrow(id)));
    }

    @PostMapping
    @Operation(summary = "Create nurse compensation type change")
    public ResponseEntity<NurseCompensationTypeChangeDto> create(@Valid @RequestBody NurseCompensationTypeChangeCreateDto createDto) {
        return ResponseEntity.ok(nurseCompensationTypeChangeMapper.toDto(nurseCompensationTypeChangeService.create(createDto)));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update nurse compensation type change")
    public ResponseEntity<NurseCompensationTypeChangeDto> update(@PathVariable UUID id,
                                             @Valid @RequestBody NurseCompensationTypeChangeUpdateDto updateDto) {
        return ResponseEntity.ok(nurseCompensationTypeChangeMapper.toDto(nurseCompensationTypeChangeService.update(id, updateDto)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete nurse compensation type change")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        nurseCompensationTypeChangeService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
