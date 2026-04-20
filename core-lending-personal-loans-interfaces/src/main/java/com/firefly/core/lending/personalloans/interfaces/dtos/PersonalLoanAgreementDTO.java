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


package com.firefly.core.lending.personalloans.interfaces.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.firefly.core.lending.personalloans.interfaces.enums.AgreementStatusEnum;
import com.firefly.core.lending.personalloans.interfaces.enums.EarlyRepaymentPenaltyTypeEnum;
import com.firefly.core.lending.personalloans.interfaces.enums.InsuranceTypeEnum;
import com.firefly.core.lending.personalloans.interfaces.enums.LoanPurposeEnum;
import com.firefly.core.lending.personalloans.interfaces.enums.RateTypeEnum;
import org.fireflyframework.utils.annotations.FilterableId;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PersonalLoanAgreementDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID personalLoanAgreementId;

    // ========================================================================
    // External References
    // ========================================================================

    @FilterableId
    @NotNull(message = "Application ID is required")
    private UUID applicationId;

    @FilterableId
    private UUID servicingCaseId;

    @FilterableId
    private UUID proposedOfferId;

    // ========================================================================
    // Personal Loan Agreement Terms
    // ========================================================================

    @NotNull(message = "Agreement status is required")
    private AgreementStatusEnum agreementStatus;

    @NotNull(message = "Loan purpose is required")
    private LoanPurposeEnum loanPurpose;

    @Size(max = 500, message = "Purpose description cannot exceed 500 characters")
    private String purposeDescription;

    // ========================================================================
    // Interest Rate Terms (Contractual - what was AGREED)
    // ========================================================================

    @NotNull(message = "Rate type is required")
    private RateTypeEnum rateType;

    @DecimalMin(value = "0.00", message = "Interest rate cannot be negative")
    @DecimalMax(value = "100.00", message = "Interest rate cannot exceed 100%")
    private BigDecimal interestRate;

    @Size(max = 50, message = "Reference rate cannot exceed 50 characters")
    private String referenceRate;

    @DecimalMin(value = "-10.00", message = "Margin rate cannot be less than -10%")
    @DecimalMax(value = "50.00", message = "Margin rate cannot exceed 50%")
    private BigDecimal marginRate;

    @Min(value = 0, message = "Fixed rate period cannot be negative")
    @Max(value = 480, message = "Fixed rate period cannot exceed 480 months")
    private Integer fixedRatePeriodMonths;

    @DecimalMin(value = "0.00", message = "Rate cap cannot be negative")
    @DecimalMax(value = "100.00", message = "Rate cap cannot exceed 100%")
    private BigDecimal rateCap;

    @DecimalMin(value = "0.00", message = "Rate floor cannot be negative")
    @DecimalMax(value = "100.00", message = "Rate floor cannot exceed 100%")
    private BigDecimal rateFloor;

    // ========================================================================
    // Security and Guarantor
    // ========================================================================

    private Boolean isUnsecured;
    private Boolean guarantorRequired;

    // ========================================================================
    // Insurance
    // ========================================================================

    private InsuranceTypeEnum insuranceType;

    @DecimalMin(value = "0.00", message = "Insurance premium rate cannot be negative")
    @DecimalMax(value = "100.00", message = "Insurance premium rate cannot exceed 100%")
    private BigDecimal insurancePremiumRate;

    @Size(max = 255, message = "Insurance provider cannot exceed 255 characters")
    private String insuranceProvider;

    @Size(max = 100, message = "Insurance policy number cannot exceed 100 characters")
    private String insurancePolicyNumber;

    // ========================================================================
    // Early Repayment
    // ========================================================================

    private EarlyRepaymentPenaltyTypeEnum earlyRepaymentPenaltyType;

    @DecimalMin(value = "0.00", message = "Early repayment penalty rate cannot be negative")
    @DecimalMax(value = "100.00", message = "Early repayment penalty rate cannot exceed 100%")
    private BigDecimal earlyRepaymentPenaltyRate;

    @Min(value = 0, message = "Early repayment penalty period cannot be negative")
    @Max(value = 480, message = "Early repayment penalty period cannot exceed 480 months")
    private Integer earlyRepaymentPenaltyPeriodMonths;

    private Boolean allowsPartialPrepayment;

    @DecimalMin(value = "0.00", message = "Partial prepayment minimum amount cannot be negative")
    private BigDecimal partialPrepaymentMinAmount;

    // ========================================================================
    // Fees
    // ========================================================================

    @DecimalMin(value = "0.00", message = "Origination fee rate cannot be negative")
    @DecimalMax(value = "100.00", message = "Origination fee rate cannot exceed 100%")
    private BigDecimal originationFeeRate;

    @DecimalMin(value = "0.00", message = "Origination fee amount cannot be negative")
    private BigDecimal originationFeeAmount;

    // ========================================================================
    // Cooling Off and Deferral
    // ========================================================================

    @Min(value = 0, message = "Cooling off period cannot be negative")
    private Integer coolingOffPeriodDays;

    @Min(value = 0, message = "Max deferral count cannot be negative")
    private Integer maxDeferralCount;

    @Min(value = 0, message = "Deferral period months cannot be negative")
    private Integer deferralPeriodMonths;

    // ========================================================================
    // Audit and Lifecycle
    // ========================================================================

    private LocalDate agreementSignedDate;
    private LocalDate agreementEffectiveDate;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String createdBy;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime createdAt;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String updatedBy;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime updatedAt;
}
