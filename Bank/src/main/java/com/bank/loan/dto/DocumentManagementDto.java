package com.bank.loan.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DocumentManagementDto {
	
	private String loanId;           	    // 貸款唯一識別碼
	private String mName;                  	// 顧客姓名（對應 member 的 Name）
	private String proofDocumentUrl;		// 圖片存取位置
	private LocalDateTime reviewTime;		// 審核時間
	private String decision; 				// 決策結果（通過 / 拒絕 / 補件中）
	private String notes; 					// 備註
	
}
