package com.bank.loan.controller;

import org.springframework.web.bind.annotation.RestController;
import com.bank.loan.dto.CreditReviewDto;
import com.bank.loan.dto.DocumentManagementDto;
import com.bank.loan.dto.LoanDetailDto;
import com.bank.loan.dto.LoansDto;
import com.bank.loan.dto.ReviewHistoryDto;
import com.bank.loan.service.CreditReviewDtoService;
import com.bank.loan.service.DocumentManagementDtoService;
import com.bank.loan.service.LoanDetailService;
import com.bank.loan.service.LoansService;
import com.bank.loan.service.ReviewHistoryService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
public class LoansController {

	@Autowired
	private LoansService lService;
	
	@Autowired
	private LoanDetailService ldService;
	
	@Autowired
	private CreditReviewDtoService adService;
	
	@Autowired
	private DocumentManagementDtoService dmdService;
	
	@Autowired
	private ReviewHistoryService rhService;
	
	@GetMapping("/loans")
	public List<LoansDto> LoansAction() {
		return lService.findAllDto();
	}
	
	@GetMapping("/loans/{loanId}")
	public ResponseEntity<LoanDetailDto> showLoanDetail(@PathVariable String loanId) {
	    LoanDetailDto dto = ldService.findLoanDetailById(loanId);
	    if(dto == null) {
	    	return ResponseEntity.notFound().build();
	    }
	    return ResponseEntity.ok(dto);
	}
	
	@GetMapping("/credit-review/{reviewId}")
    public ResponseEntity<CreditReviewDto> getCreditReviewById(@PathVariable Integer reviewId) {
        CreditReviewDto dto = adService.findByIdDto(reviewId);

        if (dto == null) {
            return ResponseEntity.notFound().build(); // 找不到就回 404
        }

        return ResponseEntity.ok(dto); // 找到就回 200 與資料
    }
	
	@GetMapping("/document-management/{reviewId}")
	public ResponseEntity<DocumentManagementDto> getDocumentManagementById(@PathVariable Integer reviewId){
		DocumentManagementDto dto = dmdService.finByIdDto(reviewId);
		
		if(dto == null) {
			return ResponseEntity.notFound().build();  // 找不到就回 404
		}
		
		return ResponseEntity.ok(dto);  // 找到就回 200 與資料
	}
	
	@GetMapping("/review/{reviewId}")
	public ResponseEntity<ReviewHistoryDto> getReviewHistoryById(@PathVariable Integer reviewId) {
		ReviewHistoryDto dto = rhService.findById(reviewId);
		
		if(dto == null){
			return ResponseEntity.notFound().build();
		}
		
		return ResponseEntity.ok(dto);
	}
	



}
