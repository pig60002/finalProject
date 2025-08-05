package com.bank.loan.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoanCreateDto {
    private String loanTypeId;     		// 貸款類型 ID
    private String loanTermId;     		// 期數分類 ID
    private int loanTerm;     			// 期數(月)
    private String repayAccountId; 		// 用戶選擇的還款帳戶ID
    
    @JsonProperty("mId")
    private Integer mId;        		// 會員 ID
    private BigDecimal loanAmount;   	// 申請金額
    private String incomeProofPath;		// 財力證明存取位置
}
