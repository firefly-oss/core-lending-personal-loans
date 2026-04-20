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

-- ========================================================================
-- V1: Create Personal Loan Schema
-- ========================================================================
-- This migration creates the complete personal loan microservice schema.
--
-- SCOPE: This microservice handles ONLY personal-loan-specific contractual
-- terms. All operational servicing (payments, disbursements, accruals,
-- notifications) is handled by the core-lending-loan-servicing microservice.
-- ========================================================================

-- ========================================================================
-- ENUMERATIONS
-- ========================================================================

-- Agreement Status
CREATE TYPE agreement_status AS ENUM (
    'DRAFT',        -- Agreement is being drafted
    'ACTIVE',       -- Agreement is active
    'SUSPENDED',    -- Agreement is temporarily suspended
    'CLOSED',       -- Agreement is closed/completed
    'CANCELLED'     -- Agreement was cancelled
);

COMMENT ON TYPE agreement_status IS 'Status of the personal loan agreement lifecycle';

-- Loan Purpose
CREATE TYPE loan_purpose AS ENUM (
    'GENERAL_PURPOSE',      -- General purpose personal loan
    'DEBT_CONSOLIDATION',   -- Consolidating existing debts
    'HOME_IMPROVEMENT',     -- Home improvement/renovation
    'MAJOR_PURCHASE',       -- Major purchase (vehicle, appliance, etc.)
    'MEDICAL_EXPENSE',      -- Medical/healthcare expenses
    'EDUCATION',            -- Education/tuition expenses
    'OTHER'                 -- Other purposes
);

COMMENT ON TYPE loan_purpose IS 'Purpose of the personal loan';

-- Rate Type
CREATE TYPE rate_type AS ENUM (
    'FIXED',        -- Fixed interest rate for entire term
    'VARIABLE',     -- Variable/adjustable rate
    'HYBRID'        -- Hybrid (fixed period then variable)
);

COMMENT ON TYPE rate_type IS 'Type of interest rate structure (contractual term)';

-- Insurance Type
CREATE TYPE insurance_type AS ENUM (
    'NONE',                 -- No insurance
    'PAYMENT_PROTECTION',   -- Payment protection insurance
    'LIFE',                 -- Life insurance
    'DISABILITY',           -- Disability insurance
    'UNEMPLOYMENT',         -- Unemployment insurance
    'COMPREHENSIVE'         -- Comprehensive coverage
);

COMMENT ON TYPE insurance_type IS 'Type of insurance associated with the personal loan';

-- Early Repayment Penalty Type
CREATE TYPE early_repayment_penalty_type AS ENUM (
    'NONE',             -- No penalty for early repayment
    'FIXED_FEE',        -- Fixed fee penalty
    'PERCENTAGE',       -- Percentage-based penalty
    'SLIDING_SCALE'     -- Sliding scale penalty (decreases over time)
);

COMMENT ON TYPE early_repayment_penalty_type IS 'Type of penalty for early repayment of the personal loan';

-- ========================================================================
-- TABLES
-- ========================================================================

-- Personal Loan Agreement
CREATE TABLE personal_loan_agreement (
    -- Primary Key
    personal_loan_agreement_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    -- External References (to other microservices)
    application_id UUID,                    -- Reference to LoanApplication (Loan Origination)
    servicing_case_id UUID,                 -- Reference to LoanServicingCase (Loan Servicing)
    proposed_offer_id UUID,                 -- Reference to ProposedOffer (Loan Origination)

    -- Personal Loan Agreement Terms
    agreement_status agreement_status NOT NULL DEFAULT 'DRAFT',
    loan_purpose loan_purpose NOT NULL,
    purpose_description VARCHAR(500),       -- Free-text description of loan purpose

    -- Interest Rate Terms (Contractual - what was AGREED)
    rate_type rate_type NOT NULL,
    interest_rate DECIMAL(5,4),             -- Interest rate (e.g., 5.5000 = 5.5%)
    reference_rate VARCHAR(50),             -- e.g., "EURIBOR_12M", "SOFR", "PRIME" (for variable/hybrid)
    margin_rate DECIMAL(5,4),               -- Margin/spread added to reference rate
    fixed_rate_period_months INTEGER,       -- For hybrid loans - initial fixed period
    rate_cap DECIMAL(5,4),                  -- Maximum interest rate cap (contractual limit)
    rate_floor DECIMAL(5,4),                -- Minimum interest rate floor (contractual limit)

    -- Security and Guarantor
    is_unsecured BOOLEAN DEFAULT TRUE,      -- Whether the loan is unsecured
    guarantor_required BOOLEAN DEFAULT FALSE, -- Whether a guarantor is required

    -- Insurance
    insurance_type insurance_type DEFAULT 'NONE',
    insurance_premium_rate DECIMAL(5,4),    -- Insurance premium rate
    insurance_provider VARCHAR(255),        -- Insurance provider name
    insurance_policy_number VARCHAR(100),   -- Insurance policy number

    -- Early Repayment
    early_repayment_penalty_type early_repayment_penalty_type DEFAULT 'NONE',
    early_repayment_penalty_rate DECIMAL(5,2), -- Penalty rate for early repayment
    early_repayment_penalty_period_months INTEGER, -- Period during which penalty applies
    allows_partial_prepayment BOOLEAN DEFAULT TRUE, -- Partial prepayments allowed per contract
    partial_prepayment_min_amount DECIMAL(15,2), -- Minimum amount for partial prepayment

    -- Fees
    origination_fee_rate DECIMAL(5,4),      -- Origination fee as percentage
    origination_fee_amount DECIMAL(15,2),   -- Origination fee as fixed amount

    -- Cooling Off and Deferral
    cooling_off_period_days INTEGER,        -- Cooling off period in days
    max_deferral_count INTEGER,             -- Maximum number of payment deferrals allowed
    deferral_period_months INTEGER,         -- Duration of each deferral period in months

    -- Audit and Lifecycle
    agreement_signed_date DATE,             -- Date agreement was signed
    agreement_effective_date DATE,          -- Date agreement becomes effective
    created_by VARCHAR(255),                -- User who created the agreement
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by VARCHAR(255),                -- User who last updated
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE personal_loan_agreement IS 'Personal loan agreement terms. Stores CONTRACTUAL terms (what was AGREED), not operational servicing data (handled by loan servicing microservice).';

-- Column Comments
COMMENT ON COLUMN personal_loan_agreement.personal_loan_agreement_id IS 'Unique identifier for the personal loan agreement';
COMMENT ON COLUMN personal_loan_agreement.application_id IS 'Reference to loan application in loan origination microservice';
COMMENT ON COLUMN personal_loan_agreement.servicing_case_id IS 'Reference to loan servicing case in loan servicing microservice';
COMMENT ON COLUMN personal_loan_agreement.proposed_offer_id IS 'Reference to proposed offer in loan origination microservice';
COMMENT ON COLUMN personal_loan_agreement.agreement_status IS 'Current status of the agreement (DRAFT, ACTIVE, SUSPENDED, CLOSED, CANCELLED)';
COMMENT ON COLUMN personal_loan_agreement.loan_purpose IS 'Purpose of the personal loan';
COMMENT ON COLUMN personal_loan_agreement.purpose_description IS 'Free-text description of the loan purpose';
COMMENT ON COLUMN personal_loan_agreement.rate_type IS 'Type of interest rate structure (FIXED, VARIABLE, HYBRID)';
COMMENT ON COLUMN personal_loan_agreement.interest_rate IS 'Interest rate at contract start (contractual term)';
COMMENT ON COLUMN personal_loan_agreement.reference_rate IS 'Reference rate index for variable/hybrid loans (e.g., EURIBOR, SOFR)';
COMMENT ON COLUMN personal_loan_agreement.margin_rate IS 'Margin/spread added to reference rate for variable/hybrid loans';
COMMENT ON COLUMN personal_loan_agreement.fixed_rate_period_months IS 'Initial fixed rate period for hybrid loans (in months)';
COMMENT ON COLUMN personal_loan_agreement.rate_cap IS 'Maximum interest rate cap (contractual limit)';
COMMENT ON COLUMN personal_loan_agreement.rate_floor IS 'Minimum interest rate floor (contractual limit)';
COMMENT ON COLUMN personal_loan_agreement.is_unsecured IS 'Whether the personal loan is unsecured (true) or secured (false)';
COMMENT ON COLUMN personal_loan_agreement.guarantor_required IS 'Whether a guarantor is required for the loan';
COMMENT ON COLUMN personal_loan_agreement.insurance_type IS 'Type of insurance associated with the personal loan';
COMMENT ON COLUMN personal_loan_agreement.insurance_premium_rate IS 'Insurance premium rate as a percentage';
COMMENT ON COLUMN personal_loan_agreement.insurance_provider IS 'Name of the insurance provider';
COMMENT ON COLUMN personal_loan_agreement.insurance_policy_number IS 'Insurance policy number';
COMMENT ON COLUMN personal_loan_agreement.early_repayment_penalty_type IS 'Type of penalty for early repayment';
COMMENT ON COLUMN personal_loan_agreement.early_repayment_penalty_rate IS 'Penalty rate for early repayment (percentage)';
COMMENT ON COLUMN personal_loan_agreement.early_repayment_penalty_period_months IS 'Period during which early repayment penalty applies (in months)';
COMMENT ON COLUMN personal_loan_agreement.allows_partial_prepayment IS 'Whether partial prepayments are allowed per contract';
COMMENT ON COLUMN personal_loan_agreement.partial_prepayment_min_amount IS 'Contractual minimum amount for partial prepayment';
COMMENT ON COLUMN personal_loan_agreement.origination_fee_rate IS 'Origination fee as a percentage of the loan amount';
COMMENT ON COLUMN personal_loan_agreement.origination_fee_amount IS 'Origination fee as a fixed amount';
COMMENT ON COLUMN personal_loan_agreement.cooling_off_period_days IS 'Cooling off period in days after signing';
COMMENT ON COLUMN personal_loan_agreement.max_deferral_count IS 'Maximum number of payment deferrals allowed';
COMMENT ON COLUMN personal_loan_agreement.deferral_period_months IS 'Duration of each deferral period in months';
COMMENT ON COLUMN personal_loan_agreement.agreement_signed_date IS 'Date the agreement was signed';
COMMENT ON COLUMN personal_loan_agreement.agreement_effective_date IS 'Date the agreement becomes effective';

-- ========================================================================
-- INDEXES
-- ========================================================================

CREATE INDEX idx_personal_loan_agreement_application_id ON personal_loan_agreement(application_id);
CREATE INDEX idx_personal_loan_agreement_servicing_case_id ON personal_loan_agreement(servicing_case_id);
CREATE INDEX idx_personal_loan_agreement_proposed_offer_id ON personal_loan_agreement(proposed_offer_id);
CREATE INDEX idx_personal_loan_agreement_loan_purpose ON personal_loan_agreement(loan_purpose);
CREATE INDEX idx_personal_loan_agreement_rate_type ON personal_loan_agreement(rate_type);
CREATE INDEX idx_personal_loan_agreement_signed_date ON personal_loan_agreement(agreement_signed_date);
CREATE INDEX idx_personal_loan_agreement_effective_date ON personal_loan_agreement(agreement_effective_date);
CREATE INDEX idx_personal_loan_agreement_status ON personal_loan_agreement(agreement_status);
