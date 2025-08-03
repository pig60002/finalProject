package com.bank.loan.bean;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "loan_terms")
public class LoanTerm {
    @Id
    @Column(name = "loan_term_id")
    private String loanTermId;

    @Column(name = "term_category")
    private String termCategory;

    @Column(name = "min_months")
    private int minMonths;

    @Column(name = "max_months")
    private int maxMonths;

    @Column(name = "term_adjustment_rate")
    private BigDecimal termAdjustmentRate;

    // getter / setter
    public String getLoanTermId() { return loanTermId; }
    public void setLoanTermId(String loanTermId) { this.loanTermId = loanTermId; }

    public String getTermCategory() { return termCategory; }
    public void setTermCategory(String termCategory) { this.termCategory = termCategory; }

    public int getMinMonths() { return minMonths; }
    public void setMinMonths(int minMonths) { this.minMonths = minMonths; }

    public int getMaxMonths() { return maxMonths; }
    public void setMaxMonths(int maxMonths) { this.maxMonths = maxMonths; }

    public BigDecimal getTermAdjustmentRate() { return termAdjustmentRate; }
    public void setTermAdjustmentRate(BigDecimal termAdjustmentRate) { this.termAdjustmentRate = termAdjustmentRate; }
}

