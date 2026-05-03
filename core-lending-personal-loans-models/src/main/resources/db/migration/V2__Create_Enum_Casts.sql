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

-- ========================================================================
-- V2: Create Enum Casts
-- ========================================================================
-- This migration creates custom casts for PostgreSQL enums to work
-- seamlessly with Spring Data R2DBC and Java enums.
-- ========================================================================

-- Agreement Status Enum Casts
CREATE CAST (varchar AS agreement_status) WITH INOUT AS IMPLICIT;
CREATE CAST (agreement_status AS varchar) WITH INOUT AS IMPLICIT;

-- Loan Purpose Enum Casts
CREATE CAST (varchar AS loan_purpose) WITH INOUT AS IMPLICIT;
CREATE CAST (loan_purpose AS varchar) WITH INOUT AS IMPLICIT;

-- Rate Type Enum Casts
CREATE CAST (varchar AS rate_type) WITH INOUT AS IMPLICIT;
CREATE CAST (rate_type AS varchar) WITH INOUT AS IMPLICIT;

-- Insurance Type Enum Casts
CREATE CAST (varchar AS insurance_type) WITH INOUT AS IMPLICIT;
CREATE CAST (insurance_type AS varchar) WITH INOUT AS IMPLICIT;

-- Early Repayment Penalty Type Enum Casts
CREATE CAST (varchar AS early_repayment_penalty_type) WITH INOUT AS IMPLICIT;
CREATE CAST (early_repayment_penalty_type AS varchar) WITH INOUT AS IMPLICIT;
