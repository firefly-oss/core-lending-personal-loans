/*
 * Copyright 2025 Firefly Software Foundation
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


package com.firefly.core.lending.personalloans.core.services;

import org.fireflyframework.core.filters.FilterRequest;
import org.fireflyframework.core.queries.PaginationResponse;
import com.firefly.core.lending.personalloans.interfaces.dtos.PersonalLoanAgreementDTO;
import reactor.core.publisher.Mono;
import java.util.UUID;
public interface PersonalLoanAgreementService {

    /**
     * Retrieves a paginated list of personal loan agreements based on the provided filter criteria.
     *
     * @param filterRequest the filter criteria and pagination details for retrieving personal loan agreements
     * @return a reactive wrapper containing a paginated response of PersonalLoanAgreementDTOs
     */
    Mono<PaginationResponse<PersonalLoanAgreementDTO>> findAll(FilterRequest<PersonalLoanAgreementDTO> filterRequest);

    /**
     * Creates a new personal loan agreement based on the provided data.
     *
     * @param dto the data transfer object containing the details of the personal loan agreement to be created
     * @return a Mono emitting the created PersonalLoanAgreementDTO instance upon successful creation
     */
    Mono<PersonalLoanAgreementDTO> create(PersonalLoanAgreementDTO dto);

    /**
     * Retrieves a specific personal loan agreement by its unique identifier.
     *
     * @param personalLoanAgreementId the unique identifier of the personal loan agreement to be retrieved
     * @return a Mono emitting the PersonalLoanAgreementDTO corresponding to the specified ID,
     *         or an empty Mono if no agreement is found
     */
    Mono<PersonalLoanAgreementDTO> getById(UUID personalLoanAgreementId);

    /**
     * Updates an existing personal loan agreement with the provided details.
     *
     * @param personalLoanAgreementId the unique identifier of the personal loan agreement to be updated
     * @param dto the data transfer object containing the updated details of the personal loan agreement
     * @return a Mono emitting the updated PersonalLoanAgreementDTO upon successful update,
     *         or an error if the update operation fails
     */
    Mono<PersonalLoanAgreementDTO> update(UUID personalLoanAgreementId, PersonalLoanAgreementDTO dto);

    /**
     * Deletes a personal loan agreement identified by its unique ID.
     *
     * @param personalLoanAgreementId the unique identifier of the personal loan agreement to be deleted
     * @return a {@code Mono<Void>} indicating the completion of the delete operation
     */
    Mono<Void> delete(UUID personalLoanAgreementId);
}
