package com.bank.loan.controller;

import org.springframework.web.bind.annotation.RestController;
import com.bank.loan.dto.CreditReviewDto;
import com.bank.loan.dto.DocumentManagementDto;
import com.bank.loan.dto.LoanDetailDto;
import com.bank.loan.dto.LoansDto;
import com.bank.loan.service.CreditReviewDtoService;
import com.bank.loan.service.DocumentManagementDtoService;
import com.bank.loan.service.LoanDetailDtoService;
import com.bank.loan.service.LoansDtoService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
public class LoansController {

	@Autowired
	private LoansDtoService lService;
	
	@Autowired
	private LoanDetailDtoService ldService;
	
	@Autowired
	private CreditReviewDtoService adService;
	
	@Autowired
	private DocumentManagementDtoService dmdService;
	
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
		DocumentManagementDto dto = dmdService.findByIdDto(reviewId);
		
		if(dto == null) {
			return ResponseEntity.notFound().build();  // 找不到就回 404
		}
		
		return ResponseEntity.ok(dto);  // 找到就回 200 與資料
	}


}
