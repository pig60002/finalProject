package com.bank.loan.bean;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "loan_types")
public class LoanType {
    @Id
    @Column(name = "loan_type_id")
    private String loanTypeId;

    @Column(name = "types_name")
    private String typesName;

    @Column(name = "base_interest_rate")
    private BigDecimal baseInterestRate;

    // getter / setter
    public String getLoanTypeId() { return loanTypeId; }
    public void setLoanTypeId(String loanTypeId) { this.loanTypeId = loanTypeId; }

    public String getTypesName() { return typesName; }
    public void setTypesName(String typesName) { this.typesName = typesName; }

    public BigDecimal getBaseInterestRate() { return baseInterestRate; }
    public void setBaseInterestRate(BigDecimal baseInterestRate) { this.baseInterestRate = baseInterestRate; }
}

