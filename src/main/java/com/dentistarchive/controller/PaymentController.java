package com.dentistarchive.controller;

import com.dentistarchive.dto.PaymentDto;
import com.dentistarchive.dto.create.PaymentCreateDto;
import com.dentistarchive.dto.update.PaymentUpdateDto;
import com.dentistarchive.mapper.PaymentMapper;
import com.dentistarchive.search.filter.PaymentFilter;
import com.dentistarchive.search.filter.SearchRequest;
import com.dentistarchive.search.filter.SearchResponse;
import com.dentistarchive.search.sort.PaymentSort;
import com.dentistarchive.service.PaymentService;
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
@Tag(name = "Payments")
@RequestMapping("/api/v1/payments")
@PreAuthorize("hasRole('DOCTOR')")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PaymentController extends BaseController {

    PaymentService paymentService;
    PaymentMapper paymentMapper;

    @GetMapping("/{id}")
    @Operation(summary = "Get payment by id")
    public ResponseEntity<PaymentDto> getPaymentById(@PathVariable UUID id) {
        return ResponseEntity.ok(paymentMapper.toDto(paymentService.getByIdOrElseThrow(id)));
    }

    @PostMapping("/search")
    @Operation(summary = "Search payment")
    public SearchResponse<PaymentDto> search(@RequestBody SearchRequest<PaymentFilter, PaymentSort> searchRequest) {
        return paymentMapper.toSearchResponse(paymentService.search(searchRequest));
    }

    @PostMapping
    @Operation(summary = "Create payment")
    public ResponseEntity<PaymentDto> create(@Valid @RequestBody PaymentCreateDto createDto) {
        return ResponseEntity.ok(paymentMapper.toDto(paymentService.create(createDto)));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update payment info")
    public ResponseEntity<PaymentDto> update(@PathVariable UUID id,
                                             @Valid @RequestBody PaymentUpdateDto updateDto) {
        return ResponseEntity.ok(paymentMapper.toDto(paymentService.update(id, updateDto)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete (archive) payment")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        paymentService.archiveById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/restore")
    @Operation(summary = "Restore (unarchive) payment")
    public ResponseEntity<PaymentDto> unarchive(@PathVariable UUID id) {
        return ResponseEntity.ok(
                paymentMapper.toDto(paymentService.unarchiveById(id))
        );
    }

}
