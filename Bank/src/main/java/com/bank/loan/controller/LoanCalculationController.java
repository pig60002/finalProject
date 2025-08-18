package com.bank.loan.controller;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bank.loan.service.LoanCalculationService;

@RestController
@RequestMapping("/loans/calc")
public class LoanCalculationController {

    @Autowired
    private LoanCalculationService calcService;

    // 即時利率 API - 加入異常處理
    @GetMapping("/rate")
    public ResponseEntity<?> getEstimatedRate(
            @RequestParam String loanTypeId,
            @RequestParam String loanTermId) {
        try {
            System.out.println("接收參數 - loanTypeId: " + loanTypeId + ", loanTermId: " + loanTermId);
            BigDecimal rate = calcService.calculateInterestRate(loanTypeId, loanTermId);
            System.out.println("計算結果 - rate: " + rate);
            return ResponseEntity.ok(rate);
        } catch (IllegalArgumentException e) {
            System.err.println("參數錯誤: " + e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            System.err.println("系統錯誤: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("error", "系統錯誤: " + e.getMessage()));
        }
    }

    // 貸款試算 API - 加入異常處理
    @PostMapping("/monthly-payment")
    public ResponseEntity<?> calculatePayment(@RequestBody LoanCalcRequest req) {
        try {
            System.out.println("接收參數 - " + req.toString());
            
            LoanCalculationService.LoanCalcResult result = calcService.calculateMonthlyPayment(
                req.getLoanTypeId(),
                req.getLoanTermId(),
                req.getLoanMonths(),
                req.getAmount()
            );
            
            System.out.println("計算結果 - monthlyPayment: " + result.getMonthlyPayment());
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            System.err.println("參數錯誤: " + e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            System.err.println("系統錯誤: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("error", "系統錯誤: " + e.getMessage()));
        }
    }

    // 建立 DTO
    public static class LoanCalcRequest {
        private String loanTypeId;
        private String loanTermId;
        private int loanMonths;
        private BigDecimal amount;

        // getter & setter
        public String getLoanTypeId() { return loanTypeId; }
        public void setLoanTypeId(String loanTypeId) { this.loanTypeId = loanTypeId; }
        public String getLoanTermId() { return loanTermId; }
        public void setLoanTermId(String loanTermId) { this.loanTermId = loanTermId; }
        public int getLoanMonths() { return loanMonths; }
        public void setLoanMonths(int loanMonths) { this.loanMonths = loanMonths; }
        public BigDecimal getAmount() { return amount; }
        public void setAmount(BigDecimal amount) { this.amount = amount; }

        @Override
        public String toString() {
            return "LoanCalcRequest{" +
                    "loanTypeId='" + loanTypeId + '\'' +
                    ", loanTermId='" + loanTermId + '\'' +
                    ", loanMonths=" + loanMonths +
                    ", amount=" + amount +
                    '}';
        }
    }
}