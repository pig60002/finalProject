package com.bank.loan.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewHistoryDto {
	
	private int reviewId; 				// 主鍵，自動遞增
	private String mName; 				// 會員姓名
	private String loanId; 				// 貸款ID（外鍵）
	private Integer reviewerId; 		// 審核人員ID（可為 NULL）
	private LocalDateTime reviewTime; 	// 審核時間
	private Integer creditScore; 		// 此次審核信用分數
	private String decision; 			// 決策結果（通過 / 拒絕 / 補件中）
	private String notes; 				// 備註

}
