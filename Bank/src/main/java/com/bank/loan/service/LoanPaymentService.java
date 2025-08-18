package com.bank.loan.service;

import com.bank.loan.bean.LoanPayment;
import com.bank.loan.dao.LoanPaymentRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoanPaymentService {
	
	@Autowired
    private LoanPaymentRepository lpRepo;

    public List<LoanPayment> getPaymentsByLoanId(String loanId) {
        return lpRepo.findByLoanId(loanId);
    }

    public LoanPayment save(LoanPayment payment) {
        return lpRepo.save(payment);
    }
    
    public List<LoanPayment> getPaymentsByMemberId(Integer mId) {
        return lpRepo.findByMemberId(mId);
    }
}
