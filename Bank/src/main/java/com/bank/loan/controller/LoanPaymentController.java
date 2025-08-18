package com.bank.loan.controller;

import com.bank.loan.bean.LoanPayment;
import com.bank.loan.bean.LoanRepaymentSchedule;
import com.bank.loan.service.LoanPaymentService;
import com.bank.loan.service.LoanRepaymentScheduleService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/loans")
public class LoanPaymentController {

	@Autowired
    private LoanPaymentService lpService;
	
	@Autowired
    private LoanRepaymentScheduleService lrsService;

    // 查詢單筆貸款的還款排程
    @GetMapping("/{loanId}/schedules")
    public List<LoanRepaymentSchedule> getSchedules(@PathVariable String loanId) {
        return lrsService.getSchedulesByLoanId(loanId);
    }

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

    // 新增還款排程
    @PostMapping("/{loanId}/schedules")
    public LoanRepaymentSchedule addSchedule(@PathVariable String loanId, @RequestBody LoanRepaymentSchedule schedule) {
        schedule.setLoanId(loanId);
        return lrsService.save(schedule);
    }
    
    // 查詢會員所有貸款繳費紀錄
    @GetMapping("/member/{mId}/payments")
    public List<LoanPayment> getPaymentsByMember(@PathVariable Integer mId) {
        return lpService.getPaymentsByMemberId(mId);
    }

}
