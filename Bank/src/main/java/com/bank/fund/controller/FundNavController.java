package com.bank.fund.controller;

import com.bank.fund.service.FundNavUpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/fundNav")
public class FundNavController {
    
    @Autowired
    private FundNavUpdateService fundNavUpdateService;
    
    /**
     * 批量更新所有活躍基金的淨值
     * @param navDate 淨值日期（可選，默認今天）
     * @return 更新結果
     */
    @PostMapping("/batch-update-all")
    public ResponseEntity<Map<String, Object>> batchUpdateAllFunds(
            @RequestParam(required = false) 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate navDate) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            FundNavUpdateService.BatchUpdateResult result = 
                fundNavUpdateService.batchUpdateAllFunds(navDate);
            
            response.put("success", true);
            response.put("message", "批量更新完成");
            response.put("totalCount", result.getTotalCount());
            response.put("successCount", result.getSuccessCount());
            response.put("skippedCount", result.getSkippedCount());
            response.put("errorCount", result.getErrorCount());
            response.put("navDate", result.getNavDate());
            response.put("successRate", String.format("%.1f%%", result.getSuccessRate()));
            response.put("isFullSuccess", result.isFullSuccess());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "批量更新失敗: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * 批量更新指定基金的淨值
     * @param fundCodes 基金代碼列表
     * @param navDate 淨值日期（可選，默認今天）
     * @return 更新結果
     */
    @PostMapping("/batch-update-specific")
    public ResponseEntity<Map<String, Object>> batchUpdateSpecificFunds(
            @RequestBody List<String> fundCodes,
            @RequestParam(required = false) 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate navDate) {
        
        Map<String, Object> response = new HashMap<>();
        
        if (fundCodes == null || fundCodes.isEmpty()) {
            response.put("success", false);
            response.put("message", "基金代碼列表不能為空");
            return ResponseEntity.badRequest().body(response);
        }
        
        try {
            FundNavUpdateService.BatchUpdateResult result = 
                fundNavUpdateService.batchUpdateSpecificFunds(fundCodes, navDate);
            
            response.put("success", true);
            response.put("message", "指定基金批量更新完成");
            response.put("requestedFunds", fundCodes);
            response.put("totalCount", result.getTotalCount());
            response.put("successCount", result.getSuccessCount());
            response.put("skippedCount", result.getSkippedCount());
            response.put("errorCount", result.getErrorCount());
            response.put("navDate", result.getNavDate());
            response.put("successRate", String.format("%.1f%%", result.getSuccessRate()));
            response.put("isFullSuccess", result.isFullSuccess());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "批量更新失敗: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * 獲取系統統計資訊
     * @param navDate 查詢日期（可選，默認今天）
     * @return 統計資訊
     */
    @GetMapping("/system-info")
    public ResponseEntity<Map<String, Object>> getSystemInfo(
            @RequestParam(required = false) 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate navDate) {
        
        Map<String, Object> response = new HashMap<>();
        
        if (navDate == null) {
            navDate = LocalDate.now();
        }
        
        try {
            int activeFundCount = fundNavUpdateService.getActiveFundCount();
            int pendingNavCount = fundNavUpdateService.getPendingNavCount(navDate);
            int completedNavCount = activeFundCount - pendingNavCount;
            
            response.put("success", true);
            response.put("queryDate", navDate);
            response.put("activeFundCount", activeFundCount);
            response.put("completedNavCount", completedNavCount);
            response.put("pendingNavCount", pendingNavCount);
            response.put("completionRate", activeFundCount > 0 ? 
                String.format("%.1f%%", (double) completedNavCount / activeFundCount * 100) : "0.0%");
            response.put("needsUpdate", pendingNavCount > 0);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "獲取系統資訊失敗: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * 健康檢查端點
     * @return 服務狀態
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            int activeFundCount = fundNavUpdateService.getActiveFundCount();
            
            response.put("status", "UP");
            response.put("service", "FundNavUpdateService");
            response.put("timestamp", LocalDate.now());
            response.put("activeFundCount", activeFundCount);
            response.put("message", "淨值更新服務運行正常");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "DOWN");
            response.put("message", "服務異常: " + e.getMessage());
            return ResponseEntity.status(503).body(response);
        }
    }
}