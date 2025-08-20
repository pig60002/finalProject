package com.bank.loan.service;

import com.bank.loan.bean.LoanRepaymentSchedule;
import com.bank.loan.bean.Loans;
import com.bank.loan.dao.LoanRepaymentScheduleRepository;
import com.bank.loan.dao.LoanRepository;
import com.bank.loan.dto.LoanRepaymentScheduleDto;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class LoanRepaymentScheduleService {

    @Autowired
    private LoanRepaymentScheduleRepository lrsRepo;

    @Autowired
    private LoanRepository loanRepo;

    // 查詢單筆貸款的排程
    public List<LoanRepaymentSchedule> getSchedulesByLoanId(String loanId) {
        return lrsRepo.findByLoanId(loanId);
    }

    // 轉成 DTO
    public List<LoanRepaymentScheduleDto> getSchedulesDtoByLoanId(String loanId) {
        return getSchedulesByLoanId(loanId).stream().map(this::toDto).toList();
    }

    // 查詢或生成排程並回傳 DTO
    public List<LoanRepaymentScheduleDto> getOrGenerateSchedulesDto(String loanId) {
        List<LoanRepaymentScheduleDto> schedulesDto = getSchedulesDtoByLoanId(loanId);

        if (schedulesDto.isEmpty()) {
            // 取得貸款資料
            Loans loan = loanRepo.findById(loanId)
                    .orElseThrow(() -> new RuntimeException("Loan not found"));

            // 生成排程
            List<LoanRepaymentSchedule> schedules = generateSchedules(loan);

            // 儲存
            if (!schedules.isEmpty()) {
                schedules = saveAll(schedules);
            }

            // 轉 DTO
            schedulesDto = schedules.stream().map(this::toDto).toList();
        }

        return schedulesDto;
    }

    // 單筆儲存
    public LoanRepaymentSchedule save(LoanRepaymentSchedule schedule) {
        return lrsRepo.save(schedule);
    }

    // 批次儲存
    public List<LoanRepaymentSchedule> saveAll(List<LoanRepaymentSchedule> schedules) {
        return lrsRepo.saveAll(schedules);
    }

    // DTO 轉換
    public LoanRepaymentScheduleDto toDto(LoanRepaymentSchedule schedule) {
        LoanRepaymentScheduleDto dto = new LoanRepaymentScheduleDto();
        dto.setScheduleId(schedule.getScheduleId());
        dto.setLoanId(schedule.getLoanId());
        dto.setInstallmentNumber(schedule.getInstallmentNumber());
        dto.setDueDate(schedule.getDueDate());
        dto.setAmountDue(schedule.getAmountDue());
        dto.setAmountPaid(schedule.getAmountPaid());
        dto.setPaymentStatus(schedule.getPaymentStatus());
        return dto;
    }

    public LoanRepaymentSchedule fromDto(LoanRepaymentScheduleDto dto) {
        LoanRepaymentSchedule schedule = new LoanRepaymentSchedule();
        schedule.setScheduleId(dto.getScheduleId());
        schedule.setLoanId(dto.getLoanId());
        schedule.setInstallmentNumber(dto.getInstallmentNumber());
        schedule.setDueDate(dto.getDueDate());
        schedule.setAmountDue(dto.getAmountDue());
        schedule.setAmountPaid(dto.getAmountPaid());
        schedule.setPaymentStatus(dto.getPaymentStatus());
        return schedule;
    }

    // 生成還款排程
    public List<LoanRepaymentSchedule> generateSchedules(Loans loan) {
        List<LoanRepaymentSchedule> schedules = new ArrayList<>();

        int termCount = loan.getLoanTerm();               // 期數
        BigDecimal principal = loan.getLoanAmount();     // 貸款本金
        BigDecimal monthlyRate = loan.getInterestRate(); // 月利率，例如 0.01

        if (termCount <= 0 || principal == null || monthlyRate == null) {
            return schedules;
        }

        // 等額本息每月還款公式: M = P * r * (1+r)^n / ((1+r)^n - 1)
        BigDecimal one = BigDecimal.ONE;
        BigDecimal numerator = principal.multiply(monthlyRate).multiply((one.add(monthlyRate)).pow(termCount));
        BigDecimal denominator = (one.add(monthlyRate)).pow(termCount).subtract(one);
        BigDecimal monthlyPayment = numerator.divide(denominator, 2, RoundingMode.HALF_UP);

        LocalDate firstDueDate = LocalDate.now().plusMonths(1); // 第一個月到期日

        for (int i = 0; i < termCount; i++) {
            LoanRepaymentSchedule schedule = new LoanRepaymentSchedule();
            schedule.setLoanId(loan.getLoanId());
            schedule.setInstallmentNumber(i + 1);
            schedule.setDueDate(firstDueDate.plusMonths(i));
            schedule.setAmountDue(monthlyPayment);
            schedule.setAmountPaid(BigDecimal.ZERO);
            schedule.setPaymentStatus("pending");
            schedules.add(schedule);
        }

        return schedules;
    }
}
