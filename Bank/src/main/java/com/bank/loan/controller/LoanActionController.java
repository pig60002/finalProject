package com.bank.loan.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.bank.loan.dto.ReviewHistoryDto;
import com.bank.loan.service.LoanActionService;

@RestController
@RequestMapping("/loanApplications")
public class LoanActionController {

	@Autowired
	private LoanActionService laService;

	// 上傳補件（如財力證明）
	@PostMapping("/{loanId}/upload-proof")
	public ResponseEntity<String> uploadSupplementDocument(@PathVariable String loanId,
			@RequestParam("file") MultipartFile file) {
		// 1. 儲存檔案
		// 2. 更新 loans.proofDocumentUrl
		try {
			laService.uploadProof(loanId, file);
			return ResponseEntity.ok("Document uploaded successfully");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(500).body("Upload failed: " + e.getMessage());
		}
	}
	
	// 更新審核狀態
	@PostMapping("/{loanId}/status")
    public ResponseEntity<?> updateLoanStatus(@PathVariable String loanId, @RequestParam String newStatus, @RequestParam Integer reviewerId) {
		laService.updateStatus(loanId, newStatus, reviewerId); // 可改成從登入者取得
        return ResponseEntity.ok("狀態已更新");
    }

	// 審核紀錄新增
	@PostMapping("/{loanId}/review")
	public ResponseEntity<String> submitReview(@PathVariable String loanId, @RequestBody ReviewHistoryDto dto) {
		// 將 dto 儲存進 credit_review_logs
		// 若需要可同時更新 loans.approvalStatus
		try {
			laService.saveReview(loanId, dto);
            return ResponseEntity.ok("Review submitted");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Review submission failed: " + e.getMessage());
        }
	}
}
