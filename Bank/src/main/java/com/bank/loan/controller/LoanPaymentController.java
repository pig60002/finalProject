package com.bank.loan.controller;

import com.bank.loan.bean.LoanPayment;
import com.bank.loan.dto.LoanPaymentDto;
import com.bank.loan.dto.LoanRepaymentScheduleDto;
import com.bank.loan.service.LoanPaymentService;
import com.bank.loan.service.LoanRepaymentScheduleService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/loans")
public class LoanPaymentController {

	@Autowired
    private LoanPaymentService lpService;
	
	@Autowired
    private LoanRepaymentScheduleService lrsService;

    // 查詢單筆貸款的還款紀錄
    @GetMapping("/{loanId}/payments")
    public List<LoanPayment> getPayments(@PathVariable String loanId) {
        return lpService.getPaymentsByLoanId(loanId);
    }

    // 新增還款紀錄
    @PostMapping("/{loanId}/payments")
    public LoanPayment addPayment(@PathVariable String loanId, @RequestBody LoanPayment payment) {
        payment.setLoanId(loanId);
        payment.setPaymentDate(java.time.LocalDateTime.now());
        return lpService.save(payment);
    }
    
    // 查詢會員所有貸款繳費紀錄
    @GetMapping("/member/{mId}/payments")
    public ResponseEntity<List<LoanPaymentDto>> getMemberPayments(@PathVariable Integer mId) {
        try {
            List<LoanPaymentDto> dtoList = lpService.getPaymentsByMemberIdDto(mId);
            return ResponseEntity.ok(dtoList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // 查詢與建立還款排程
    @GetMapping("/{loanId}/schedules")
    public ResponseEntity<List<LoanRepaymentScheduleDto>> getOrGenerateSchedules(@PathVariable String loanId) {
        try {
            List<LoanRepaymentScheduleDto> schedulesDto = lrsService.getOrGenerateSchedulesDto(loanId);
            return ResponseEntity.ok(schedulesDto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // 新增繳費 API (會更新排程)
    @PostMapping("/{loanId}/payments/submit")
    public ResponseEntity<LoanPayment> submitPayment(
            @PathVariable String loanId,
            @RequestBody LoanPayment payment) {

        try {
            payment.setLoanId(loanId);
            payment.setPaymentDate(java.time.LocalDateTime.now());
            LoanPayment savedPayment = lpService.savePaymentAndUpdateSchedule(payment);
            return ResponseEntity.ok(savedPayment);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 取得下一期未繳排程
    @GetMapping("/{loanId}/schedules/next")
    public ResponseEntity<LoanRepaymentScheduleDto> getNextSchedule(@PathVariable String loanId) {
        try {
            LoanRepaymentScheduleDto next = lpService.getNextScheduleDto(loanId);
            return ResponseEntity.ok(next);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }





}
