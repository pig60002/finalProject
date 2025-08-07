package com.bank.loan.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.bank.account.bean.Account;
import com.bank.loan.bean.Loans;
import com.bank.loan.dto.DocumentManagementDto;
import com.bank.loan.dto.LoanCreateDto;
import com.bank.loan.dto.ReviewHistoryDto;
import com.bank.loan.service.DocumentManagementDtoService;
import com.bank.loan.service.LoanProcessingService;
import com.bank.loan.service.LoanService;

@RestController
@RequestMapping("/loans")
public class LoanManagementController {

    @Autowired
    private LoanService loanService;
    
	@Autowired
	private LoanProcessingService lpService;
	
	@Autowired
	private DocumentManagementDtoService dmdService;

	// 上傳補件（如財力證明）
	@PostMapping("/{loanId}/upload-proof")
	public ResponseEntity<String> uploadSupplementDocument(@PathVariable String loanId,
			@RequestParam("file") MultipartFile file) {
		// 1. 儲存檔案
		// 2. 更新 loans.proofDocumentUrl
		try {
			lpService.uploadProof(loanId, file);
			return ResponseEntity.ok("Document uploaded successfully");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(500).body("Upload failed: " + e.getMessage());
		}
	}
	
	// 更新審核狀態
	@PostMapping("/{loanId}/status")
    public ResponseEntity<?> updateLoanStatus(@PathVariable String loanId, @RequestBody Map<String, Object> body) {
		String newStatus = (String) body.get("newStatus");
	    Integer reviewerId = (Integer) body.get("reviewerId");
	    String notes = (String) body.get("notes");
		lpService.updateStatus(loanId, newStatus, reviewerId, notes); // 可改成從登入者取得
		return ResponseEntity.ok("狀態已更新");
    }

	// 審核紀錄新增
	@PostMapping("/{loanId}/review")
	public ResponseEntity<String> submitReview(@PathVariable String loanId, @RequestBody ReviewHistoryDto dto) {
		// 將 dto 儲存進 credit_review_logs
		// 若需要可同時更新 loans.approvalStatus
		try {
			lpService.saveReview(loanId, dto);
            return ResponseEntity.ok("Review submitted");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Review submission failed: " + e.getMessage());
        }
	}

    @GetMapping("/accounts")
    public ResponseEntity<List<Account>> getAccountsByMemberId(@RequestParam Integer mId) {
        List<Account> accounts = loanService.getAccountsByMemberId(mId);
        return ResponseEntity.ok(accounts);
    }

    /**
     * 新增貸款（一定會有檔案）
     * 前端表單請用 enctype="multipart/form-data"
     * 欄位名稱需為：loan, file
     */
    @PostMapping("/create")
    public ResponseEntity<?> createLoan(
            @RequestPart("loan") LoanCreateDto dto,
            @RequestPart("file") MultipartFile file) {

        try {
            Loans savedLoan = loanService.createLoan(dto, file);
            return ResponseEntity.ok(savedLoan);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("檔案上傳失敗");
        } catch (Exception e) {
            e.printStackTrace(); // 開發用，之後可拿掉
            return ResponseEntity.internalServerError().body("新增貸款失敗");
        }
    }
    
    // 取得審核紀錄
    @GetMapping("/{loanId}/latest-review")
    public ResponseEntity<DocumentManagementDto> getLatestReview(@PathVariable String loanId) {
        DocumentManagementDto dto = dmdService.findLatestByLoanId(loanId);
        if (dto != null) {
            return ResponseEntity.ok(dto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
