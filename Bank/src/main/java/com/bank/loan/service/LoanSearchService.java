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


    public List<LoansDto> searchLoansByMemberName(String keyword) {
        List<Loans> loans = lRepo.findByMember_mNameContainingIgnoreCase(keyword);
        return loans.stream()
                .map(LoansDto::new)
                .collect(Collectors.toList());
    }

}
