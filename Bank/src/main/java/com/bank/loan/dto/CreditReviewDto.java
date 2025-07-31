package com.bank.loan.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreditReviewDto {
	
	private String loanId;				// 貸款唯一識別碼
	private String mName;					// 顧客姓名（從 Member 關聯取得）
	private Integer reviewerId; 		// 審核人員ID（可為 NULL）
	private Integer reviewedCreditScore;// 信用分數來自 CreditReviewLogs
	private String decision; 			// 決策結果（通過 / 拒絕 / 補件中）
	private String notes; 				// 備註
	
}
