package com.bank.fund.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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