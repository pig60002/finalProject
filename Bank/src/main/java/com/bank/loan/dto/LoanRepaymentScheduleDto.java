package com.bank.loan.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoanRepaymentScheduleDto {
	
	private Long scheduleId;         // 還款排程唯一識別碼
    private String loanId;           // 貸款唯一識別碼
    private int installmentNumber;   // 分期編號（第幾期）
    private LocalDate dueDate;       // 應還款日期
    private BigDecimal amountDue;    // 應還金額
    private BigDecimal amountPaid;   // 已還金額
    private String paymentStatus;    // 還款狀態（pending: 未還, paid: 已還, overdue: 逾期）

}
