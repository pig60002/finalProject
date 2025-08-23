package com.bank.loan.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoanPaymentDto {
	
	private Long paymentId;          // 還款紀錄唯一識別碼
    private String loanId;           // 貸款唯一識別碼
    private Long scheduleId;         // 對應的還款排程ID（可為 null 表示非指定排程的提前還款）
    private LocalDateTime paymentDate; // 實際付款日期時間
    private BigDecimal amountPaid;   // 實際付款金額
    private String paymentMethod;    // 付款方式（如轉帳、現金、信用卡）
    private String paymentReference; // 付款參考號碼（交易編號或銀行回傳編號）
    
    private LocalDateTime createdAt; // 建立時間

}
