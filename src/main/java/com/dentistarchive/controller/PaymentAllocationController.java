package com.dentistarchive.controller;

import com.dentistarchive.dto.PaymentAllocationDto;
import com.dentistarchive.dto.create.PaymentAllocationCreateDto;
import com.dentistarchive.dto.update.PaymentAllocationUpdateDto;
import com.dentistarchive.mapper.PaymentAllocationMapper;
import com.dentistarchive.search.filter.PaymentAllocationFilter;
import com.dentistarchive.search.filter.SearchRequest;
import com.dentistarchive.search.filter.SearchResponse;
import com.dentistarchive.search.sort.PaymentAllocationSort;
import com.dentistarchive.service.PaymentAllocationService;
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
@Tag(name = "Payment Allocations")
@RequestMapping("/api/v1/payment-allocations")
@PreAuthorize("hasRole('DOCTOR')")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PaymentAllocationController extends BaseController {

    PaymentAllocationService paymentAllocationService;
    PaymentAllocationMapper paymentAllocationMapper;

    @GetMapping("/{id}")
    @Operation(summary = "Get payment allocation by id")
    public ResponseEntity<PaymentAllocationDto> getPaymentAllocationById(@PathVariable UUID id) {
        return ResponseEntity.ok(paymentAllocationMapper.toDto(paymentAllocationService.getByIdOrElseThrow(id)));
    }

    @PostMapping("/search")
    @Operation(summary = "Search payment allocations")
    public SearchResponse<PaymentAllocationDto> search(@RequestBody SearchRequest<PaymentAllocationFilter, PaymentAllocationSort> searchRequest) {
        return paymentAllocationMapper.toSearchResponse(paymentAllocationService.search(searchRequest));
    }

    @PostMapping
    @Operation(summary = "Create payment allocation")
    public ResponseEntity<PaymentAllocationDto> create(@Valid @RequestBody PaymentAllocationCreateDto createDto) {
        return ResponseEntity.ok(paymentAllocationMapper.toDto(paymentAllocationService.create(createDto)));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update payment allocation")
    public ResponseEntity<PaymentAllocationDto> update(@PathVariable UUID id,
                                             @Valid @RequestBody PaymentAllocationUpdateDto updateDto) {
        return ResponseEntity.ok(paymentAllocationMapper.toDto(paymentAllocationService.update(id, updateDto)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete (archive) payment allocation")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        paymentAllocationService.archiveById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/restore")
    @Operation(summary = "Restore (unarchive) payment allocation")
    public ResponseEntity<PaymentAllocationDto> unarchive(@PathVariable UUID id) {
        return ResponseEntity.ok(
                paymentAllocationMapper.toDto(paymentAllocationService.unarchiveById(id))
        );
    }

}
