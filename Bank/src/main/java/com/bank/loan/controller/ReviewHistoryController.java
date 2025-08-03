package com.bank.loan.controller;

import com.bank.loan.dto.ReviewHistoryDto;
import com.bank.loan.service.ReviewHistoryDtoService;
import com.bank.loan.service.ReviewHistoryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/review") // 統一開頭
public class ReviewHistoryController {

    @Autowired
    private ReviewHistoryDtoService rhdService;

    @Autowired
    private ReviewHistoryService rhService;

    // 可選：單筆查詢（如果你之後要做單筆顯示/編輯）
    @GetMapping("/{reviewId}")
    public ResponseEntity<ReviewHistoryDto> getReviewHistoryById(@PathVariable Integer reviewId) {
        ReviewHistoryDto dto = rhdService.findById(reviewId);

        if (dto == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(dto);
    }

    // 儲存或更新審核紀錄
    @PostMapping("/save")
    public ResponseEntity<String> saveOrUpdateReview(@RequestBody ReviewHistoryDto dto) {
        rhService.saveOrUpdateFromDto(dto);
        return ResponseEntity.ok("Review history saved or updated successfully.");
    }
}
