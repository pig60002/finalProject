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


/**
 * LoanQueryController 負責處理與貸款相關的查詢 API 請求
 * 
 * 這個 Controller 提供多個 GET 端點，讓前端可以查詢貸款清單、
 * 貸款詳細資料、信用審核資訊以及文件管理資料。
 */
@RestController
public class LoanQueryController {

	// 注入 LoansDtoService 用於查詢貸款清單
	@Autowired
	private LoansDtoService lService;
	
	// 注入 LoanDetailDtoService 用於查詢貸款詳細資料
	@Autowired
	private LoanDetailDtoService ldService;
	
	// 注入 CreditReviewDtoService 用於查詢信用審核資料
	@Autowired
	private CreditReviewDtoService adService;
	
	// 注入 DocumentManagementDtoService 用於查詢文件管理資料
	@Autowired
	private DocumentManagementDtoService dmdService;
	
	/**
	 * GET /loans
	 * 查詢所有貸款資料，並回傳 List<LoansDto>
	 * 
	 * @return 所有貸款清單
	 */
	@GetMapping("/loans")
	public List<LoansDto> LoansAction() {
		return lService.findAllDto();
	}
	
	/**
	 * GET /loans/{loanId}
	 * 根據貸款ID查詢貸款詳細資料
	 * 
	 * @param loanId 貸款ID
	 * @return 如果找到資料回傳 200 與 LoanDetailDto，
	 *         找不到則回傳 404 Not Found
	 */
	@GetMapping("/loans/{loanId}")
	public ResponseEntity<LoanDetailDto> showLoanDetail(@PathVariable String loanId) {
	    LoanDetailDto dto = ldService.findLoanDetailById(loanId);
	    if(dto == null) {
	    	return ResponseEntity.notFound().build();  // 找不到資料回 404
	    }
	    return ResponseEntity.ok(dto);  // 找到資料回 200
	}
	
	/**
	 * GET /credit-review/{reviewId}
	 * 根據信用審核ID查詢信用審核資料
	 * 
	 * @param reviewId 信用審核ID
	 * @return 如果找到資料回傳 200 與 CreditReviewDto，
	 *         找不到則回傳 404 Not Found
	 */
	@GetMapping("/credit-review/{reviewId}")
    public ResponseEntity<CreditReviewDto> getCreditReviewById(@PathVariable Integer reviewId) {
        CreditReviewDto dto = adService.findByIdDto(reviewId);

        if (dto == null) {
            return ResponseEntity.notFound().build(); // 找不到就回 404
        }

        return ResponseEntity.ok(dto); // 找到就回 200 與資料
    }
	
	/**
	 * GET /document-management/{reviewId}
	 * 根據文件管理ID查詢文件管理資料
	 * 
	 * @param reviewId 文件管理ID
	 * @return 如果找到資料回傳 200 與 DocumentManagementDto，
	 *         找不到則回傳 404 Not Found
	 */
	@GetMapping("/document-management/{reviewId}")
	public ResponseEntity<DocumentManagementDto> getDocumentManagementById(@PathVariable Integer reviewId){
		DocumentManagementDto dto = dmdService.findByIdDto(reviewId);
		
		if(dto == null) {
			return ResponseEntity.notFound().build();  // 找不到就回 404
		}
		
		return ResponseEntity.ok(dto);  // 找到就回 200 與資料
	}
	
	// 依據會員ID查詢貸款資料
	@GetMapping("/loans/member/{mid}")
	public List<LoansDto> getMemberLoans(@PathVariable (required = false) Integer mid) {
	    if(mid != null) {
	        return lService.findByMemberId(mid);
	    }
	    return lService.findAllDto();
	}

}
