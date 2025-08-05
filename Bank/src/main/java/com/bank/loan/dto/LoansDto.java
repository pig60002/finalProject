package com.bank.loan.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.bank.loan.bean.Loans;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoansDto {
	
	private String loanId;                // 貸款唯一識別碼
	private String mName;                 // 顧客
	private String loanTypeId;           // 貸款類型 ID
	private String loanTypeName;         // 貸款類型名稱
	private String loanTermId;           // 期數分類 ID
	private int loanTerm;                // 貸款期數（通常以月為單位）
	private BigDecimal loanAmount;      // 貸款金額
	private BigDecimal interestRate;    // 實際利率
	private String repayAccountId;      // 還款帳戶ID
	private LocalDate loanstartDate; 	// 貸款開始時間
	private String approvalStatus;      // 審核狀態
	private String approvalStatusName;  // 審核狀態-中文
	
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDateTime createdAt;    // 建立時間

	public LoansDto(Loans loan) {
		this.loanId = loan.getLoanId();
        this.mName = loan.getMember().getmName();
        this.loanTypeId = loan.getLoanTypeId();
        this.loanTypeName = convertLoanTypeName(loan.getLoanTypeId());
        this.loanTermId = loan.getLoanTermId();
        this.loanTerm = loan.getLoanTerm();
        this.loanAmount = loan.getLoanAmount();
        this.interestRate = loan.getInterestRate();
        this.repayAccountId = loan.getRepayAccountId();
        this.loanstartDate = loan.getLoanstartDate();
        this.approvalStatus = loan.getApprovalStatus();
        this.approvalStatus = convertApprovalStatus(loan.getApprovalStatus());
        this.createdAt = loan.getCreatedAt();
	} 
	
	// 貸款類型的轉換方法
    private String convertLoanTypeName(String loanTypeId) {
        switch(loanTypeId) {
            case "LT001": return "車貸";
            case "LT002": return "房貸";
            case "LT003": return "學貸";
            default: return "未知類型";
        }
    }
    
 
    // 審核狀態的轉換方法
    private String convertApprovalStatus(String approvalStatus) {
        switch(approvalStatus) {
            case "approved": return "審核通過";
            case "supplement": return "補件中";
            case "rejected": return "拒絕申請";
            case "pending": return "待審核";
            default: return "未知類型";
        }
    }
	

}
