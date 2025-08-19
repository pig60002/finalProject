package com.bank.loan.service;

import com.bank.loan.bean.LoanPayment;
import com.bank.loan.dao.LoanPaymentRepository;
import com.bank.loan.dto.LoanPaymentDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
    
 // 查會員繳款資料並轉 DTO
    public List<LoanPaymentDto> getPaymentsByMemberIdDto(Integer mId) {
        List<LoanPayment> payments = lpRepo.findByMemberId(mId);

        return payments.stream().map(p -> {
            LoanPaymentDto dto = new LoanPaymentDto();
            dto.setPaymentId(p.getPaymentId());
            dto.setLoanId(p.getLoanId());
            dto.setScheduleId(p.getScheduleId());
            dto.setPaymentDate(p.getPaymentDate());
            dto.setAmountPaid(p.getAmountPaid());
            dto.setPaymentMethod(p.getPaymentMethod());
            dto.setPaymentReference(p.getPaymentReference());
            dto.setCreatedAt(p.getCreatedAt());
            return dto;
        }).collect(Collectors.toList());
    }
}
