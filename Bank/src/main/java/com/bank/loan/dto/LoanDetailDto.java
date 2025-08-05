package com.bank.loan.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class LoanDetailDto {
	
	private String loanId;                // 貸款唯一識別碼
	private int mId;                      // 顧客 ID（對應 member 的主鍵）
	private String loanTypeId;           // 貸款類型 ID
	private String loanTermId;           // 期數分類 ID
	private int loanTerm;                // 貸款期數（通常以月為單位）
	private BigDecimal loanAmount;      // 貸款金額
	private BigDecimal interestRate;    // 實際利率
	private String repayAccountId; 		// 用戶選擇的還款帳戶ID
	private LocalDate loanstartDate; 	// 貸款開始時間
	private String approvalStatus;      // 審核狀態
	private LocalDateTime createdAt;    // 建立時間
	private String proofDocumentUrl;	// 圖片存取位置
	private String mName;				// 客戶姓名
	private String mIdentity;			// 客戶身分證
	private String mPhone;				// 客戶電話
	private String mEmail;				// 客戶email
	private String mAddress;			// 客戶地址
	private String employerName; 		// 公司名稱or雇主名稱
	private String occupationType;		// 職業類型
	private int yearsOfService;			// 年資
	private BigDecimal monthlyIncome; 	// 每月收入
	private BigDecimal monthlyDebt; 	// 每月債務支出
	private BigDecimal dtiRatio; 		// 負債收入比
	private Integer baseCreditScore; 	// 信用分數來自 CreditProfiles
	private Integer reviewerId; 		// 審核人員ID（可為 NULL）
	private LocalDateTime reviewTime;	// 審核時間
	private Integer reviewedCreditScore;// 信用分數來自 CreditReviewLogs
	private String decision; 			// 決策結果（通過 / 拒絕 / 補件中）
	private String notes; 				// 備註
	
}
