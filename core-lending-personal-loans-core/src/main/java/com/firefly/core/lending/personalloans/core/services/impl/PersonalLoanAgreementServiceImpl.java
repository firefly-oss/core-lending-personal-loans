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


package com.firefly.core.lending.personalloans.core.services.impl;

import org.fireflyframework.core.filters.FilterRequest;
import org.fireflyframework.core.filters.FilterUtils;
import org.fireflyframework.core.queries.PaginationResponse;
import com.firefly.core.lending.personalloans.core.services.PersonalLoanAgreementService;
import com.firefly.core.lending.personalloans.models.entities.PersonalLoanAgreement;
import com.firefly.core.lending.personalloans.models.repositories.PersonalLoanAgreementRepository;
import com.firefly.core.lending.personalloans.core.mappers.PersonalLoanAgreementMapper;
import com.firefly.core.lending.personalloans.interfaces.dtos.PersonalLoanAgreementDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import java.util.UUID;
@Service
@Transactional
public class PersonalLoanAgreementServiceImpl implements PersonalLoanAgreementService {

    @Autowired
    private PersonalLoanAgreementRepository repository;

    @Autowired
    private PersonalLoanAgreementMapper mapper;

    @Override
    public Mono<PaginationResponse<PersonalLoanAgreementDTO>> findAll(FilterRequest<PersonalLoanAgreementDTO> filterRequest) {
        return FilterUtils.createFilter(
                PersonalLoanAgreement.class,
                mapper::toDTO
        ).filter(filterRequest);
    }

    @Override
    public Mono<PersonalLoanAgreementDTO> create(PersonalLoanAgreementDTO dto) {
        PersonalLoanAgreement entity = mapper.toEntity(dto);
        return Mono.just(entity)
                .flatMap(repository::save)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<PersonalLoanAgreementDTO> getById(UUID personalLoanAgreementId) {
        return repository.findById(personalLoanAgreementId)
                .map(mapper::toDTO)
                .switchIfEmpty(Mono.empty());
    }

    @Override
    public Mono<PersonalLoanAgreementDTO> update(UUID personalLoanAgreementId, PersonalLoanAgreementDTO dto) {
        return repository.findById(personalLoanAgreementId)
                .flatMap(existingAgreement -> {
                    PersonalLoanAgreement updatedEntity = mapper.toEntity(dto);
                    updatedEntity.setPersonalLoanAgreementId(existingAgreement.getPersonalLoanAgreementId());
                    return repository.save(updatedEntity);
                })
                .map(mapper::toDTO);
    }

    @Override
    public Mono<Void> delete(UUID personalLoanAgreementId) {
        return repository.findById(personalLoanAgreementId)
                .flatMap(repository::delete);
    }
}
