package com.dentistarchive.controller;

import com.dentistarchive.dto.TechnicianMonthlyReportDto;
import com.dentistarchive.dto.update.TechnicianMonthlyReportUpdateDto;
import com.dentistarchive.mapper.TechnicianMonthlyReportMapper;
import com.dentistarchive.search.filter.SearchRequest;
import com.dentistarchive.search.filter.SearchResponse;
import com.dentistarchive.search.filter.TechnicianMonthlyReportFilter;
import com.dentistarchive.search.sort.TechnicianMonthlyReportSort;
import com.dentistarchive.service.TechnicianMonthlyReportService;
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
@Tag(name = "Technician Monthly Report")
@RequestMapping("/api/v1/technician-monthly-report")
@PreAuthorize("hasRole('DOCTOR')")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TechnicianMonthlyReportController extends BaseController {

    TechnicianMonthlyReportService technicianMonthlyReportService;
    TechnicianMonthlyReportMapper technicianMonthlyReportMapper;

    @GetMapping("/{id}")
    @Operation(summary = "Get technician monthly report by id")
    public ResponseEntity<TechnicianMonthlyReportDto> getTechnicianMonthlyReportById(@PathVariable UUID id) {
        return ResponseEntity.ok(technicianMonthlyReportMapper.toDto(technicianMonthlyReportService.getByIdOrElseThrow(id)));
    }

    @PostMapping("/search")
    @Operation(summary = "Search technician monthly reports")
    public SearchResponse<TechnicianMonthlyReportDto> search(@RequestBody SearchRequest<TechnicianMonthlyReportFilter, TechnicianMonthlyReportSort> searchRequest) {
        return technicianMonthlyReportMapper.toSearchResponse(technicianMonthlyReportService.search(searchRequest));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update technician monthly report info")
    public ResponseEntity<TechnicianMonthlyReportDto> update(@PathVariable UUID id,
                                                             @Valid @RequestBody TechnicianMonthlyReportUpdateDto updateDto) {
        return ResponseEntity.ok(technicianMonthlyReportMapper.toDto(technicianMonthlyReportService.update(id, updateDto)));
    }


}

