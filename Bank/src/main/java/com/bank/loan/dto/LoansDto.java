package com.bank.loan.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.bank.loan.bean.Loans;
import com.bank.loan.enums.ApprovalStatusEnum;
import com.bank.loan.enums.LoanTypeEnum;
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
	private String approvalStatus;      // 審核狀態
	private String approvalStatusName;  // 審核狀態-中文
	private String proofDocumentUrl;    // 圖片存取位置
	private String getontractPath;    	// 貸款合約存取位置
	
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDateTime createdAt;    // 建立時間
	
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDateTime updatedAt;    // 建立時間
	
	private BigDecimal paidAmount;        // 已繳金額
	private Double progress;              // 還款進度百分比

	public LoansDto(Loans loan) {
		this.loanId = loan.getLoanId();
        this.mName = loan.getMember().getmName();
        this.loanTypeId = loan.getLoanTypeId();
        this.loanTypeName = LoanTypeEnum.fromId(loan.getLoanTypeId());
        this.loanTermId = loan.getLoanTermId();
        this.loanTerm = loan.getLoanTerm();
        this.loanAmount = loan.getLoanAmount();
        this.interestRate = loan.getInterestRate();
        this.repayAccountId = loan.getRepayAccountId();
        this.approvalStatus = loan.getApprovalStatus();
        this.approvalStatusName = ApprovalStatusEnum.fromCode(loan.getApprovalStatus());
        this.createdAt = loan.getCreatedAt();
        this.updatedAt = loan.getUpdatedAt();
        this.proofDocumentUrl = loan.getProofDocumentUrl();
        this.getontractPath = loan.getContractPath();
        
        this.paidAmount = BigDecimal.ZERO;
        this.progress = 0.0;
	} 
}
