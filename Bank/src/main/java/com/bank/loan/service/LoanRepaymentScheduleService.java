package com.bank.loan.service;

import com.bank.loan.bean.LoanRepaymentSchedule;
import com.bank.loan.dao.LoanRepaymentScheduleRepository;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@Transactional
public class LoanRepaymentScheduleService {
	
	@Autowired
    private LoanRepaymentScheduleRepository lrsRepo;

    public List<LoanRepaymentSchedule> getSchedulesByLoanId(String loanId) {
        return lrsRepo.findByLoanId(loanId);
    }

    public LoanRepaymentSchedule save(LoanRepaymentSchedule schedule) {
        return lrsRepo.save(schedule);
    }
}
