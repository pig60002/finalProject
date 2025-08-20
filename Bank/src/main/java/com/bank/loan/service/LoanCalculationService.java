package com.bank.loan.service;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bank.loan.bean.LoanTerm;
import com.bank.loan.bean.LoanType;
import com.bank.loan.dao.LoanTermRepository;
import com.bank.loan.dao.LoanTypeRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class LoanCalculationService {

    @Autowired
    private LoanTypeRepository loanTypeRepo;

    @Autowired
    private LoanTermRepository loanTermRepo;

    /**
     * 計算利率
     */
    public BigDecimal calculateInterestRate(String loanTypeId, String loanTermId) {
        LoanType type = loanTypeRepo.findById(loanTypeId)
                .orElseThrow(() -> new IllegalArgumentException("找不到貸款類型"));
        LoanTerm term = loanTermRepo.findById(loanTermId)
                .orElseThrow(() -> new IllegalArgumentException("找不到貸款期數"));

        return type.getBaseInterestRate().add(term.getTermAdjustmentRate());
    }

    /**
     * 計算每月還款金額
     * @param loanTypeId 貸款類型 ID
     * @param loanTermId 期數 ID
     * @param loanMonths 實際貸款月數（可由前端傳入）
     * @param amount 貸款金額
     */
    public LoanCalcResult calculateMonthlyPayment(String loanTypeId, String loanTermId, int loanMonths, BigDecimal amount) {
        BigDecimal rate = calculateInterestRate(loanTypeId, loanTermId);

        // 月利率 = 年利率 / 12 / 100
        BigDecimal monthlyRate = rate.divide(BigDecimal.valueOf(12 * 100), 8, RoundingMode.HALF_UP);

        // 等額本息公式：M = P * r * (1+r)^n / ((1+r)^n -1)
        BigDecimal numerator = amount.multiply(monthlyRate).multiply((BigDecimal.ONE.add(monthlyRate)).pow(loanMonths));
        BigDecimal denominator = (BigDecimal.ONE.add(monthlyRate)).pow(loanMonths).subtract(BigDecimal.ONE);
        BigDecimal monthlyPayment = numerator.divide(denominator, 2, RoundingMode.HALF_UP);

        BigDecimal totalPayment = monthlyPayment.multiply(BigDecimal.valueOf(loanMonths));
        BigDecimal totalInterest = totalPayment.subtract(amount);

        return new LoanCalcResult(monthlyPayment, totalInterest, totalPayment, rate);
    }

    // DTO for calculation result
    public static class LoanCalcResult {
        private BigDecimal monthlyPayment;
        private BigDecimal totalInterest;
        private BigDecimal totalPayment;
        private BigDecimal interestRate;

        public LoanCalcResult(BigDecimal monthlyPayment, BigDecimal totalInterest, BigDecimal totalPayment, BigDecimal interestRate) {
            this.monthlyPayment = monthlyPayment;
            this.totalInterest = totalInterest;
            this.totalPayment = totalPayment;
            this.interestRate = interestRate;
        }

        // getter
        public BigDecimal getMonthlyPayment() { return monthlyPayment; }
        public BigDecimal getTotalInterest() { return totalInterest; }
        public BigDecimal getTotalPayment() { return totalPayment; }
        public BigDecimal getInterestRate() { return interestRate; }
    }
}


