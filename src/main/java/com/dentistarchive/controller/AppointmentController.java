package com.dentistarchive.controller;

import com.dentistarchive.dto.AppointmentDto;
import com.dentistarchive.dto.create.AppointmentCreateDto;
import com.dentistarchive.dto.update.AppointmentUpdateDto;
import com.dentistarchive.mapper.AppointmentMapper;
import com.dentistarchive.search.filter.AppointmentFilter;
import com.dentistarchive.search.filter.SearchRequest;
import com.dentistarchive.search.filter.SearchResponse;
import com.dentistarchive.search.sort.AppointmentSort;
import com.dentistarchive.service.AppointmentService;
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
@Tag(name = "Appointment")
@RequestMapping("/api/v1/appointments")
@PreAuthorize("hasRole('DOCTOR')")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AppointmentController extends BaseController {

    AppointmentService appointmentService;
    AppointmentMapper appointmentMapper;

    @GetMapping("/{id}")
    @Operation(summary = "Get appointment by id")
    public ResponseEntity<AppointmentDto> getAppointmentById(@PathVariable UUID id) {
        return ResponseEntity.ok(appointmentMapper.toDto(appointmentService.getByIdOrElseThrow(id)));
    }

    @PostMapping("/search")
    @Operation(summary = "Search appointment")
    public SearchResponse<AppointmentDto> search(@RequestBody SearchRequest<AppointmentFilter, AppointmentSort> searchRequest) {
        return appointmentMapper.toSearchResponse(appointmentService.search(searchRequest));
    }

    @PostMapping
    @Operation(summary = "Create appointment")
    public ResponseEntity<AppointmentDto> create(@Valid @RequestBody AppointmentCreateDto createDto) {
        return ResponseEntity.ok(appointmentMapper.toDto(appointmentService.create(createDto)));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update appointment info")
    public ResponseEntity<AppointmentDto> update(@PathVariable UUID id,
                                             @Valid @RequestBody AppointmentUpdateDto updateDto) {
        return ResponseEntity.ok(appointmentMapper.toDto(appointmentService.update(id, updateDto)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete (archive) appointment")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        appointmentService.archiveById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/restore")
    @Operation(summary = "Restore (unarchive) appointment")
    public ResponseEntity<AppointmentDto> unarchive(@PathVariable UUID id) {
        return ResponseEntity.ok(
                appointmentMapper.toDto(appointmentService.unarchiveById(id))
        );
    }

}
