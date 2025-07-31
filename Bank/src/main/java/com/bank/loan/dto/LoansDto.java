package com.bank.loan.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.bank.loan.bean.Loans;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoansDto {
	
	private String loanId;                // 貸款唯一識別碼
	private String mNmae;                 // 顧客
	private String loanTypeId;           // 貸款類型 ID
	private String loanTermId;           // 期數分類 ID
	private int loanTerm;                // 貸款期數（通常以月為單位）
	private BigDecimal loanAmount;      // 貸款金額
	private BigDecimal interestRate;    // 實際利率
	private LocalDate loanstartDate; 	// 貸款開始時間
	private String approvalStatus;      // 審核狀態
	private LocalDateTime createdAt;    // 建立時間

	public LoansDto(Loans loan) {
		this.loanId = loan.getLoanId();
        this.mNmae = loan.getMember().getmName();
        this.loanTypeId = loan.getLoanTypeId();
        this.loanTermId = loan.getLoanTermId();
        this.loanTerm = loan.getLoanTerm();
        this.loanAmount = loan.getLoanAmount();
        this.interestRate = loan.getInterestRate();
        this.loanstartDate = loan.getLoanstartDate();
        this.approvalStatus = loan.getApprovalStatus();
        this.createdAt = loan.getCreatedAt();
	}
	

}
