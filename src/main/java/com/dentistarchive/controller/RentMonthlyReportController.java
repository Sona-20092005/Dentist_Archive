package com.dentistarchive.controller;

import com.dentistarchive.dto.RentMonthlyReportDto;
import com.dentistarchive.dto.update.RentMonthlyReportUpdateDto;
import com.dentistarchive.mapper.RentMonthlyReportMapper;
import com.dentistarchive.search.filter.RentMonthlyReportFilter;
import com.dentistarchive.search.filter.SearchRequest;
import com.dentistarchive.search.filter.SearchResponse;
import com.dentistarchive.search.sort.RentMonthlyReportSort;
import com.dentistarchive.service.RentMonthlyReportService;
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
@Tag(name = "Rent Monthly Report")
@RequestMapping("/api/v1/rent-monthly-report")
@PreAuthorize("hasRole('DOCTOR')")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RentMonthlyReportController extends BaseController {

    RentMonthlyReportService rentMonthlyReportService;
    RentMonthlyReportMapper rentMonthlyReportMapper;

    @GetMapping("/{id}")
    @Operation(summary = "Get rent monthly report by id")
    public ResponseEntity<RentMonthlyReportDto> getRentMonthlyReportById(@PathVariable UUID id) {
        return ResponseEntity.ok(rentMonthlyReportMapper.toDto(rentMonthlyReportService.getByIdOrElseThrow(id)));
    }

    @PostMapping("/search")
    @Operation(summary = "Search rent monthly reports")
    public SearchResponse<RentMonthlyReportDto> search(@RequestBody SearchRequest<RentMonthlyReportFilter, RentMonthlyReportSort> searchRequest) {
        return rentMonthlyReportMapper.toSearchResponse(rentMonthlyReportService.search(searchRequest));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update rent monthly report info")
    public ResponseEntity<RentMonthlyReportDto> update(@PathVariable UUID id,
                                                             @Valid @RequestBody RentMonthlyReportUpdateDto updateDto) {
        return ResponseEntity.ok(rentMonthlyReportMapper.toDto(rentMonthlyReportService.update(id, updateDto)));
    }


}

