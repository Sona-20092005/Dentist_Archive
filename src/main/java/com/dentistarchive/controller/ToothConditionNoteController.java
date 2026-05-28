package com.dentistarchive.controller;

import com.dentistarchive.dto.ToothConditionNoteDto;
import com.dentistarchive.dto.create.ToothConditionNoteCreateDto;
import com.dentistarchive.dto.update.ToothConditionNoteUpdateDto;
import com.dentistarchive.mapper.ToothConditionNoteMapper;
import com.dentistarchive.service.ToothConditionNoteService;
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
@Tag(name = "Tooth")
@RequestMapping("/api/v1/tooth-condition")
@PreAuthorize("hasRole('DOCTOR')")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ToothConditionNoteController extends BaseController {

    ToothConditionNoteService toothConditionNoteService;
    ToothConditionNoteMapper toothConditionNoteMapper;

    @GetMapping("/{id}")
    @Operation(summary = "Get tooth note by id")
    public ResponseEntity<ToothConditionNoteDto> getToothConditionNoteById(@PathVariable UUID id) {
        return ResponseEntity.ok(toothConditionNoteMapper.toDto(toothConditionNoteService.getByIdOrElseThrow(id)));
    }

    @PostMapping
    @Operation(summary = "Create tooth note")
    public ResponseEntity<ToothConditionNoteDto> createPatient(@Valid @RequestBody ToothConditionNoteCreateDto createDto) {
        return ResponseEntity.ok(toothConditionNoteMapper.toDto(toothConditionNoteService.create(createDto)));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update tooth note description")
    public ResponseEntity<ToothConditionNoteDto> update(@PathVariable UUID id,
                                             @Valid @RequestBody ToothConditionNoteUpdateDto updateDto) {
        return ResponseEntity.ok(toothConditionNoteMapper.toDto(toothConditionNoteService.update(id, updateDto)));
    }

}
