package com.bank.loan.service;

import com.bank.loan.dto.LoansDto;
import com.bank.loan.bean.Loans;
import com.bank.loan.dao.LoanRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class LoanSearchService {
	
	@Autowired
    private LoanRepository lRepo;

	// 依據name欄位模糊查詢用戶資料
    public List<LoansDto> searchLoansByMemberName(String keyword) {
        List<Loans> loans = lRepo.findByMember_mNameContainingIgnoreCase(keyword);
        return loans.stream()
                .map(LoansDto::new)
                .collect(Collectors.toList());
    }
    
    
    // 依據status欄位查詢用戶狀態資料
    public List<LoansDto> searchLoansByApprovalStatus(String approvalStatus) {
        List<Loans> loans = lRepo.findByApprovalStatus(approvalStatus);
        return loans.stream()
                .map(LoansDto::new)
                .collect(Collectors.toList());
    }
    
    
    public List<LoansDto> findAllLoans() {
        return lRepo.findAll()
                    .stream()
                    .map(LoansDto::new)
                    .collect(Collectors.toList());
    }

    public List<LoansDto> searchLoansByNameAndStatus(String keyword, String approvalStatus) {
        return lRepo.findByMember_mNameContainingIgnoreCaseAndApprovalStatus(keyword, approvalStatus)
                    .stream()
                    .map(LoansDto::new)
                    .collect(Collectors.toList());
    }


}
