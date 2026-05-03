# Core Lending Personal Loans Microservice

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![Java](https://img.shields.io/badge/Java-25-orange.svg)](https://openjdk.java.net/projects/jdk/25/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-green.svg)](https://spring.io/projects/spring-boot)

## Overview

The **Core Lending Personal Loans Microservice** is a specialized component of the **Firefly OpenCore Banking Platform**, developed by **Firefly Software Foundation** under the Apache 2.0 license. This microservice is responsible for managing **personal-loan-specific contractual terms**, including loan purpose, insurance, early repayment penalties, and cooling-off periods.

This service provides a focused, cohesive domain model for personal loan agreements, handling only the contractual and legal aspects that are unique to personal loan products. All operational servicing (payments, disbursements, accruals, notifications, rate changes) is delegated to the **core-lending-loan-servicing** microservice.

**Organization**: [firefly-oss](https://github.com/firefly-oss)  
**Website**: [getfirefly.io](https://getfirefly.io)  
**License**: Apache 2.0

## Architecture

### Microservices Separation of Concerns

The Firefly lending platform follows a clear separation of concerns across multiple microservices:

| Microservice | Responsibility |
|--------------|----------------|
| **core-lending-loan-origination** | Loan applications, credit analysis, underwriting, proposed offers |
| **core-lending-mortgages** | Mortgage-specific contractual terms and legal documentation |
| **core-lending-personal-loans** | **Personal-loan-specific contractual terms, insurance, and early repayment** |
| **core-lending-loan-servicing** | Operational servicing: payments, disbursements, accruals, notifications, rate changes, installments, balances |
| **core-lending-collateral-management** | Collateral valuation, insurance, property management |

### What This Microservice Does

**Personal Loan Agreement Management**
- Personal-loan-specific contractual terms
- Loan purpose classification (debt consolidation, home improvement, medical, education)
- Security and guarantor requirements
- Unsecured loan handling

**Interest Rate Contractual Terms**
- Interest rate at contract signing
- Rate type (fixed, variable, hybrid)
- Reference rate and margin (for variable/hybrid loans)
- Rate caps and floors (contractual limits)
- Fixed rate period for hybrid loans

**Insurance (Payment Protection)**
- Payment Protection Insurance (PPI)
- Life, disability, and unemployment coverage
- Comprehensive insurance options
- Insurance provider and policy tracking

**Early Repayment Terms**
- Early repayment penalty types (none, fixed fee, percentage, sliding scale)
- Penalty rates and applicable periods
- Partial prepayment permissions and minimum amounts

**Regulatory Compliance**
- Cooling-off period management
- Payment deferral terms and limits
- Origination fee tracking

### What This Microservice Does NOT Do

**Operational Servicing** (handled by core-lending-loan-servicing)
- Payment schedules and installments
- Actual payment processing
- Disbursements
- Interest accruals
- Rate changes (operational changes to current rate)
- Balance tracking
- Notifications
- Escrow management
- Rebates and commissions

**Loan Origination** (handled by core-lending-loan-origination)
- Loan applications
- Credit analysis
- Underwriting
- Proposed offers

**Collateral Management** (handled by core-lending-collateral-management)
- Property valuation
- Insurance management
- Collateral tracking

## Technology Stack

- **Java 25**: Latest LTS version with virtual threads support
- **Spring Boot 3.x**: Modern Spring framework with native compilation support
- **Spring WebFlux**: Reactive web framework for non-blocking I/O
- **R2DBC**: Reactive database connectivity for PostgreSQL
- **PostgreSQL**: Primary database with advanced features
- **Flyway**: Database migration and versioning
- **Maven**: Build automation and dependency management
- **OpenAPI 3**: API documentation and client generation
- **MapStruct**: Type-safe bean mapping
- **Lombok**: Boilerplate code reduction
- **Docker**: Containerization and deployment

## Data Model

The microservice manages a single, focused entity:

### PersonalLoanAgreement

The core entity representing personal-loan-specific contractual terms.

**Key Fields:**

**External References:**
- `applicationId`: Reference to LoanApplication (Loan Origination)
- `servicingCaseId`: Reference to LoanServicingCase (Loan Servicing)
- `proposedOfferId`: Reference to ProposedOffer (Loan Origination)

**Personal Loan Agreement Terms:**
- `agreementStatus`: DRAFT, ACTIVE, SUSPENDED, CLOSED, CANCELLED
- `loanPurpose`: Purpose of the personal loan (7 categories)
- `purposeDescription`: Free-text description of the loan purpose

**Interest Rate Contractual Terms:**
- `rateType`: FIXED, VARIABLE, HYBRID
- `interestRate`: Interest rate at contract start
- `referenceRate`: Reference rate index (for variable/hybrid loans)
- `marginRate`: Margin/spread added to reference rate
- `fixedRatePeriodMonths`: Initial fixed period for hybrid loans
- `rateCap`: Maximum interest rate cap (contractual limit)
- `rateFloor`: Minimum interest rate floor (contractual limit)

**Security and Guarantor:**
- `isUnsecured`: Whether the loan is unsecured
- `guarantorRequired`: Whether a guarantor is required

**Insurance:**
- `insuranceType`: Type of insurance coverage (PPI, life, disability, etc.)
- `insurancePremiumRate`: Premium rate for insurance
- `insuranceProvider`: Insurance provider name
- `insurancePolicyNumber`: Policy reference number

**Early Repayment:**
- `earlyRepaymentPenaltyType`: Type of penalty (none, fixed fee, percentage, sliding scale)
- `earlyRepaymentPenaltyRate`: Contractual penalty rate for early repayment
- `earlyRepaymentPenaltyPeriodMonths`: Period during which penalty applies
- `allowsPartialPrepayment`: Partial prepayments allowed per contract
- `partialPrepaymentMinAmount`: Minimum amount for partial prepayment

**Fees:**
- `originationFeeRate`: Origination fee as a percentage
- `originationFeeAmount`: Origination fee as a fixed amount

**Cooling Off and Deferral:**
- `coolingOffPeriodDays`: Regulatory cooling-off period in days
- `maxDeferralCount`: Maximum number of payment deferrals allowed
- `deferralPeriodMonths`: Duration of each deferral period

**Audit Fields:**
- `agreementSignedDate`: Date agreement was signed
- `agreementEffectiveDate`: Date agreement becomes effective
- `createdBy`, `createdAt`, `updatedBy`, `updatedAt`: Audit trail

## Enumerations

### AgreementStatusEnum

Agreement lifecycle status:
- **DRAFT**: Agreement is being drafted
- **ACTIVE**: Agreement is active
- **SUSPENDED**: Agreement is temporarily suspended
- **CLOSED**: Agreement is closed/completed
- **CANCELLED**: Agreement was cancelled

### LoanPurposeEnum

Purpose of the personal loan:
- **GENERAL_PURPOSE**: General purpose loan
- **DEBT_CONSOLIDATION**: Consolidating existing debts
- **HOME_IMPROVEMENT**: Home improvement or renovation
- **MAJOR_PURCHASE**: Major purchase (vehicle, appliance, etc.)
- **MEDICAL_EXPENSE**: Medical or healthcare expenses
- **EDUCATION**: Education or training costs
- **OTHER**: Other purpose

### RateTypeEnum

Interest rate structure:
- **FIXED**: Fixed interest rate for entire term
- **VARIABLE**: Variable/adjustable rate
- **HYBRID**: Hybrid (fixed period then variable)

### InsuranceTypeEnum

Insurance coverage type:
- **NONE**: No insurance coverage
- **PAYMENT_PROTECTION**: Payment Protection Insurance (PPI)
- **LIFE**: Life insurance coverage
- **DISABILITY**: Disability insurance coverage
- **UNEMPLOYMENT**: Unemployment insurance coverage
- **COMPREHENSIVE**: Comprehensive coverage (life, disability, and unemployment)

### EarlyRepaymentPenaltyTypeEnum

Early repayment penalty structure:
- **NONE**: No early repayment penalty
- **FIXED_FEE**: Fixed fee for early repayment
- **PERCENTAGE**: Percentage of outstanding balance
- **SLIDING_SCALE**: Penalty decreases over time

## Module Structure

The microservice follows a clean, modular architecture:

- **`core-lending-personal-loans-interfaces`**: DTOs, interfaces, enums, and API contracts
- **`core-lending-personal-loans-models`**: Entities, repositories, and database migrations
- **`core-lending-personal-loans-core`**: Business logic, service implementations, and mappers
- **`core-lending-personal-loans-web`**: REST controllers, web configuration, and application entry point
- **`core-lending-personal-loans-sdk`**: Generated client SDK for external integrations

## Prerequisites

- **Java Development Kit (JDK) 25**
- **Maven 3.8+** for build management
- **PostgreSQL 13+** for database
- **Docker** (optional, for containerized deployment)
- **Git** for version control

## Setup and Installation

### Local Development

1. **Clone the repository:**
   ```bash
   git clone git@github.com:firefly-oss/core-lending-personal-loans.git
   cd core-lending-personal-loans
   ```

2. **Set up environment variables:**
   ```bash
   export DB_HOST=localhost
   export DB_PORT=5432
   export DB_NAME=personal_loans
   export DB_USERNAME=your_username
   export DB_PASSWORD=your_password
   export DB_SSL_MODE=disable
   ```

3. **Build the project:**
   ```bash
   mvn clean install
   ```

4. **Run the application:**
   ```bash
   mvn spring-boot:run -pl core-lending-personal-loans-web
   ```

5. **Access the application:**
   - Application: http://localhost:8091
   - API Documentation: http://localhost:8091/swagger-ui.html
   - Health Check: http://localhost:8091/actuator/health

## API Documentation

The microservice provides REST APIs documented with OpenAPI 3.0:

- **Local Environment**: http://localhost:8091/swagger-ui.html

### API Endpoints

| Resource | Base Path | Description |
|----------|-----------|-------------|
| Personal Loan Agreements | `/api/v1/personal-loan-agreements` | Personal loan agreement management |

## Development Guidelines

### Coding Standards

- **Java 25 Features**: Utilize modern Java features
- **Reactive Programming**: Use Project Reactor for non-blocking operations
- **Validation**: All DTOs include comprehensive Jakarta validation annotations
- **Documentation**: Document all public APIs with OpenAPI 3.0 annotations
- **Testing**: Maintain high test coverage
- **Code Quality**: Follow Google Java Style Guide

### Database Guidelines

- **Migrations**: All schema changes must be versioned using Flyway migrations
- **UUIDs**: Use UUID primary keys for all entities
- **Enums**: Database enums are mapped to Java enums with automatic casting
- **Auditing**: All entities include audit timestamps

## Testing

```bash
# Run all tests
mvn clean test

# Run tests for specific module
mvn test -pl core-lending-personal-loans-core

# Run integration tests
mvn verify

# Run tests with coverage
mvn clean test jacoco:report
```

## License

This project is licensed under the **Apache License 2.0** - see the [LICENSE](LICENSE) file for details.

```
Copyright 2025 Firefly Software Foundation

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

## Support and Contact

- **Website**: [getfirefly.io](https://getfirefly.io)
- **GitHub Organization**: [firefly-oss](https://github.com/firefly-oss)
- **Documentation**: [docs.getfirefly.io](https://docs.getfirefly.io)
- **Community**: [community.getfirefly.io](https://community.getfirefly.io)

---

**Firefly OpenCore Banking Platform** - Building the future of open banking infrastructure.
