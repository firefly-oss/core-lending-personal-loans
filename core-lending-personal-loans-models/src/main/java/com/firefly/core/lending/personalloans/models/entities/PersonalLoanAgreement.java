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


package com.firefly.core.lending.personalloans.models.entities;

import com.firefly.core.lending.personalloans.interfaces.enums.AgreementStatusEnum;
import com.firefly.core.lending.personalloans.interfaces.enums.EarlyRepaymentPenaltyTypeEnum;
import com.firefly.core.lending.personalloans.interfaces.enums.InsuranceTypeEnum;
import com.firefly.core.lending.personalloans.interfaces.enums.LoanPurposeEnum;
import com.firefly.core.lending.personalloans.interfaces.enums.RateTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("personal_loan_agreement")
public class PersonalLoanAgreement {

    @Id
    @Column("personal_loan_agreement_id")
    private UUID personalLoanAgreementId;

    // ========================================================================
    // External References (to other microservices)
    // ========================================================================

    @Column("application_id")
    private UUID applicationId;  // Reference to LoanApplication (Loan Origination)

    @Column("servicing_case_id")
    private UUID servicingCaseId;  // Reference to LoanServicingCase (Loan Servicing)

    @Column("proposed_offer_id")
    private UUID proposedOfferId;  // Reference to ProposedOffer (Loan Origination)

    // ========================================================================
    // Personal Loan Agreement Terms
    // ========================================================================

    @Column("agreement_status")
    private AgreementStatusEnum agreementStatus;

    @Column("loan_purpose")
    private LoanPurposeEnum loanPurpose;

    @Column("purpose_description")
    private String purposeDescription;

    // ========================================================================
    // Interest Rate Terms (Contractual - what was AGREED)
    // ========================================================================

    @Column("rate_type")
    private RateTypeEnum rateType;

    @Column("interest_rate")
    private BigDecimal interestRate;

    @Column("reference_rate")
    private String referenceRate;

    @Column("margin_rate")
    private BigDecimal marginRate;

    @Column("fixed_rate_period_months")
    private Integer fixedRatePeriodMonths;

    @Column("rate_cap")
    private BigDecimal rateCap;

    @Column("rate_floor")
    private BigDecimal rateFloor;

    // ========================================================================
    // Security and Guarantor
    // ========================================================================

    @Column("is_unsecured")
    private Boolean isUnsecured;

    @Column("guarantor_required")
    private Boolean guarantorRequired;

    // ========================================================================
    // Insurance
    // ========================================================================

    @Column("insurance_type")
    private InsuranceTypeEnum insuranceType;

    @Column("insurance_premium_rate")
    private BigDecimal insurancePremiumRate;

    @Column("insurance_provider")
    private String insuranceProvider;

    @Column("insurance_policy_number")
    private String insurancePolicyNumber;

    // ========================================================================
    // Early Repayment
    // ========================================================================

    @Column("early_repayment_penalty_type")
    private EarlyRepaymentPenaltyTypeEnum earlyRepaymentPenaltyType;

    @Column("early_repayment_penalty_rate")
    private BigDecimal earlyRepaymentPenaltyRate;

    @Column("early_repayment_penalty_period_months")
    private Integer earlyRepaymentPenaltyPeriodMonths;

    @Column("allows_partial_prepayment")
    private Boolean allowsPartialPrepayment;

    @Column("partial_prepayment_min_amount")
    private BigDecimal partialPrepaymentMinAmount;

    // ========================================================================
    // Fees
    // ========================================================================

    @Column("origination_fee_rate")
    private BigDecimal originationFeeRate;

    @Column("origination_fee_amount")
    private BigDecimal originationFeeAmount;

    // ========================================================================
    // Cooling Off and Deferral
    // ========================================================================

    @Column("cooling_off_period_days")
    private Integer coolingOffPeriodDays;

    @Column("max_deferral_count")
    private Integer maxDeferralCount;

    @Column("deferral_period_months")
    private Integer deferralPeriodMonths;

    // ========================================================================
    // Audit and Lifecycle
    // ========================================================================

    @Column("agreement_signed_date")
    private LocalDate agreementSignedDate;

    @Column("agreement_effective_date")
    private LocalDate agreementEffectiveDate;

    @Column("created_by")
    private String createdBy;

    @Column("created_at")
    private LocalDateTime createdAt;

    @Column("updated_by")
    private String updatedBy;

    @Column("updated_at")
    private LocalDateTime updatedAt;
}
