/*
 * Copyright 2025 Firefly Software Solutions Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.firefly.core.lending.personalloans.web.controllers;

import org.fireflyframework.core.filters.FilterRequest;
import org.fireflyframework.core.queries.PaginationResponse;
import com.firefly.core.lending.personalloans.core.services.PersonalLoanAgreementService;
import com.firefly.core.lending.personalloans.interfaces.dtos.PersonalLoanAgreementDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.UUID;

import jakarta.validation.Valid;@RestController
@RequestMapping("/api/v1/personal-loan-agreements")
@Tag(name = "PersonalLoanAgreement", description = "Operations on personal loan agreements")
@RequiredArgsConstructor
public class PersonalLoanAgreementController {

    private final PersonalLoanAgreementService service;

    @GetMapping
    @Operation(summary = "List or search personal loan agreements")
    public Mono<ResponseEntity<PaginationResponse<PersonalLoanAgreementDTO>>> findAll(
            @Valid @RequestBody FilterRequest<PersonalLoanAgreementDTO> filterRequest) {
        return service.findAll(filterRequest).map(ResponseEntity::ok);
    }

    @PostMapping
    @Operation(summary = "Create a new personal loan agreement")
    public Mono<ResponseEntity<PersonalLoanAgreementDTO>> create(
            @Valid @RequestBody PersonalLoanAgreementDTO dto) {
        return service.create(dto).map(ResponseEntity::ok);
    }

    @GetMapping("/{personalLoanAgreementId}")
    @Operation(summary = "Get a personal loan agreement by ID")
    public Mono<ResponseEntity<PersonalLoanAgreementDTO>> getById(
            @PathVariable UUID personalLoanAgreementId) {
        return service.getById(personalLoanAgreementId).map(ResponseEntity::ok);
    }

    @PutMapping("/{personalLoanAgreementId}")
    @Operation(summary = "Update a personal loan agreement")
    public Mono<ResponseEntity<PersonalLoanAgreementDTO>> update(
            @PathVariable UUID personalLoanAgreementId,
            @Valid @RequestBody PersonalLoanAgreementDTO dto) {
        return service.update(personalLoanAgreementId, dto).map(ResponseEntity::ok);
    }

    @DeleteMapping("/{personalLoanAgreementId}")
    @Operation(summary = "Delete a personal loan agreement")
    public Mono<ResponseEntity<Void>> delete(
            @PathVariable UUID personalLoanAgreementId) {
        return service.delete(personalLoanAgreementId)
                .thenReturn(ResponseEntity.noContent().build());
    }
}
