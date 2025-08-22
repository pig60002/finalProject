package com.bank.loan.service;

import com.bank.loan.bean.LoanPayment;
import com.bank.loan.dao.LoanPaymentRepository;
import com.bank.loan.dto.LoanPaymentDto;
import com.bank.loan.dto.LoanRepaymentScheduleDto;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class LoanPaymentService {

    @Autowired
    private LoanPaymentRepository lpRepo;

    @Autowired
    private LoanRepaymentScheduleService lrsService; // 新增依賴

    public List<LoanPayment> getPaymentsByLoanId(String loanId) {
        return lpRepo.findByLoanId(loanId);
    }

    public LoanPayment save(LoanPayment payment) {
        return lpRepo.save(payment);
    }

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

    // --- 新增方法: 繳款後更新排程 ---
    public LoanPayment savePaymentAndUpdateSchedule(LoanPayment payment) {
        // 1. 儲存繳款
        LoanPayment savedPayment = lpRepo.save(payment);

        // 2. 如果有排程ID，更新排程已繳金額與狀態
        if (payment.getScheduleId() != null) {
            try {
                lrsService.updateScheduleAfterPayment(payment.getScheduleId(), payment.getAmountPaid());
            } catch(Exception e) {
                throw new RuntimeException("更新排程失敗", e);
            }
        }


        return savedPayment;
    }

    // --- 新增方法: 取得下一期未繳排程 DTO ---
    public LoanRepaymentScheduleDto getNextScheduleDto(String loanId) {
        var next = lrsService.getNextPendingSchedule(loanId);
        return next != null ? lrsService.toDto(next) : null;
    }
}

