package com.bank.fund.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.bank.account.bean.Account;
import com.bank.fund.entity.Fund;
import com.bank.fund.service.FundService;

@RestController
@RequestMapping(path = "/fund")
public class FundController {
    
    @Autowired
    private FundService fundService;
    
    @GetMapping
    public ResponseEntity<List<Fund>> getAllFunds() {
        List<Fund> funds = fundService.getAllFunds();
        return ResponseEntity.ok(funds);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Fund> getFundById(@PathVariable Integer id) {
        return fundService.getFundById(id)
            .map(fund -> ResponseEntity.ok(fund))
            .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<?> createFund(@RequestBody Fund fund) {
        try {
            Fund createdFund = fundService.createFund(fund);
            return ResponseEntity.ok(createdFund);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                .body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(new ErrorResponse("系統發生錯誤：" + e.getMessage()));
        }
    }
    
    @PostMapping("/with-nav")
    public ResponseEntity<?> createFundWithNav(@RequestBody Map<String, Object> request) {
        try {
            Fund fund = new Fund();
            
            // 安全的字串轉換
            fund.setFundCode(String.valueOf(request.get("fundCode")));
            fund.setFundName(String.valueOf(request.get("fundName")));
            fund.setFundType(String.valueOf(request.get("fundType")));
            fund.setCurrency(String.valueOf(request.get("currency")));
            fund.setStatus(String.valueOf(request.get("status")));
            
            // 數字類型處理
            fund.setRiskLevel(((Number) request.get("riskLevel")).intValue());
            fund.setSize(new BigDecimal(request.get("size").toString()));
            fund.setMinBuy(new BigDecimal(request.get("minBuy").toString()));
            fund.setBuyFee(new BigDecimal(request.get("buyFee").toString()));
            
            // 處理 launchTime
            if (request.get("launchTime") != null) {
                fund.setLaunchTime(LocalDateTime.parse(request.get("launchTime").toString()));
            }
            
            // 處理 account
            if (request.get("account") != null) {
                Map<String, Object> accountMap = (Map<String, Object>) request.get("account");
                Account account = new Account();
                // 確保 accountId 正確轉換
                Object accountIdObj = accountMap.get("accountId");
                account.setAccountId((String.valueOf(accountIdObj)));
                
                fund.setAccount(account);
            }
            
            // 取得淨值資料
            BigDecimal initialNav = new BigDecimal(request.get("initialNav").toString());
            LocalDate navDate = LocalDate.parse(request.get("navDate").toString());
            
            Fund savedFund = fundService.createFundWithInitialNav(fund, initialNav, navDate);
            
            return ResponseEntity.ok(savedFund);
        } catch (Exception e) {
            System.err.println("建立基金失敗: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest()
                .body(Map.of("message", "建立基金失敗: " + e.getMessage(), "success", false));
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> updateFund(@PathVariable Integer id, @RequestBody Fund fund) {
        try {
            Fund updatedFund = fundService.updateFund(id, fund);
            return ResponseEntity.ok(updatedFund);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                .body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(new ErrorResponse("系統發生錯誤：" + e.getMessage()));
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFund(@PathVariable Integer id) {
        try {
            fundService.deleteFund(id);
            return ResponseEntity.ok(new SuccessResponse("基金刪除成功"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                .body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(new ErrorResponse("系統發生錯誤：" + e.getMessage()));
        }
    }
    
    @GetMapping("/exists/{fundCode}")
    public ResponseEntity<Boolean> checkFundCodeExists(@PathVariable String fundCode) {
        boolean exists = fundService.existsByFundCode(fundCode);
        return ResponseEntity.ok(exists);
    }
    
    // 內部類別用於回應格式
    public static class ErrorResponse {
        private String message;
        private boolean success = false;
        
        public ErrorResponse(String message) {
            this.message = message;
        }
        
        public String getMessage() {
            return message;
        }
        
        public boolean isSuccess() {
            return success;
        }
    }
    
    public static class SuccessResponse {
        private String message;
        private boolean success = true;
        
        public SuccessResponse(String message) {
            this.message = message;
        }
        
        public String getMessage() {
            return message;
        }
        
        public boolean isSuccess() {
            return success;
        }
    }
}