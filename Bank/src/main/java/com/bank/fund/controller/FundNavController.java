package com.bank.fund.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.bank.fund.entity.FundNav;
import com.bank.fund.service.FundNavService;

@RestController
@RequestMapping(path = "/fundNav")
public class FundNavController {
    
    @Autowired
    private FundNavService fundNavService;
    
    /**
     * 根據基金ID取得淨值歷史
     */
    @GetMapping
    public ResponseEntity<List<FundNav>> getFundNavsByFundId(@RequestParam Integer fundId) {
        List<FundNav> navs = fundNavService.getByFundId(fundId);
        return ResponseEntity.ok(navs);
    }
    
    /**
     * 根據淨值ID取得單筆淨值記錄
     */
    @GetMapping("/{id}")
    public ResponseEntity<FundNav> getFundNavById(@PathVariable Integer id) {
        Optional<FundNav> nav = fundNavService.getById(id);
        return nav.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * 新增淨值記錄
     */
    @PostMapping
    public ResponseEntity<?> createFundNav(@RequestBody FundNav fundNav) {
        try {
            FundNav createdNav = fundNavService.create(fundNav);
            return ResponseEntity.ok(createdNav);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                .body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(new ErrorResponse("系統發生錯誤：" + e.getMessage()));
        }
    }
    
    /**
     * 更新淨值記錄
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateFundNav(@PathVariable Integer id, @RequestBody FundNav fundNav) {
        try {
            FundNav updatedNav = fundNavService.update(id, fundNav);
            return ResponseEntity.ok(updatedNav);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                .body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(new ErrorResponse("系統發生錯誤：" + e.getMessage()));
        }
    }
    
    /**
     * 刪除淨值記錄
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFundNav(@PathVariable Integer id) {
        try {
            fundNavService.delete(id);
            return ResponseEntity.ok(new SuccessResponse("淨值記錄刪除成功"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                .body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(new ErrorResponse("系統發生錯誤：" + e.getMessage()));
        }
    }
    
    /**
     * 手動觸發每日淨值更新（當日）
     */
    @PostMapping("/update/daily")
    public ResponseEntity<?> updateDailyNav() {
        try {
            fundNavService.updateDailyNav();
            return ResponseEntity.ok(new SuccessResponse("每日淨值更新完成"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(new ErrorResponse("淨值更新失敗：" + e.getMessage()));
        }
    }
    
    /**
     * 手動觸發指定日期的淨值更新
     */
    @PostMapping("/update/daily/{date}")
    public ResponseEntity<?> updateDailyNavForDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        try {
            fundNavService.updateDailyNav(date);
            return ResponseEntity.ok(new SuccessResponse("指定日期淨值更新完成：" + date));
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest()
                .body(new ErrorResponse("日期格式錯誤，請使用 YYYY-MM-DD 格式"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(new ErrorResponse("淨值更新失敗：" + e.getMessage()));
        }
    }
    
    /**
     * 手動更新單一基金的淨值
     */
    @PostMapping("/update/single")
    public ResponseEntity<?> updateSingleFundNav(
            @RequestParam Integer fundId,
            @RequestParam BigDecimal nav,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate navDate) {
        try {
            FundNav updatedNav = fundNavService.updateSingleFundNav(fundId, nav, navDate);
            return ResponseEntity.ok(updatedNav);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                .body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(new ErrorResponse("淨值更新失敗：" + e.getMessage()));
        }
    }
    
    /**
     * 批量更新淨值記錄
     */
    @PostMapping("/batch")
    public ResponseEntity<?> createBatchFundNavs(@RequestBody List<FundNav> fundNavs) {
        try {
            if (fundNavs == null || fundNavs.isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(new ErrorResponse("淨值清單不能為空"));
            }
            
            List<FundNav> createdNavs = fundNavService.createOrUpdateBatch(fundNavs);
            return ResponseEntity.ok(createdNavs);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(new ErrorResponse("批量處理失敗：" + e.getMessage()));
        }
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