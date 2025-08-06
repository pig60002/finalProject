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

    // GET /loans?search=keyword
    @GetMapping("/search/loans")
    public List<LoansDto> searchLoans(@RequestParam(required = false, name = "search") String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            // 不輸入關鍵字時，回傳全部（可選，視需求調整）
            return lsService.searchLoansByMemberName("");
        } else {
            return lsService.searchLoansByMemberName(keyword);
        }
    }
}
