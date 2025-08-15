package com.bank.loan.controller;

import com.bank.loan.dto.LoansDto;
import com.bank.loan.service.LoanSearchService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class LoanSearchController {

	@Autowired
    private LoanSearchService lsService;

	@GetMapping("/search/loans")
	public List<LoansDto> searchLoans(
	    @RequestParam(required = false, name = "search") String keyword,
	    @RequestParam(required = false, name = "status") String status) {

	    // 兩個條件都空 → 查全部
	    if ((keyword == null || keyword.trim().isEmpty()) && (status == null || status.trim().isEmpty())) {
	        return lsService.findAllLoans(); // 你要自己加一個查全部的方法
	    }

	    // 只傳 status
	    if (keyword == null || keyword.trim().isEmpty()) {
	        return lsService.searchLoansByApprovalStatus(status);
	    }

	    // 只傳 keyword
	    if (status == null || status.trim().isEmpty()) {
	        return lsService.searchLoansByMemberName(keyword);
	    }

	    // 同時傳 keyword 和 status → 兩個條件一起篩
	    return lsService.searchLoansByNameAndStatus(keyword, status);
	}

}
