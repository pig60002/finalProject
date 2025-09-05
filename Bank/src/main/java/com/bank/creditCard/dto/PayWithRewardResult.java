package com.bank.creditCard.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class PayWithRewardResult {
	 private String status;            // COMPLETED / PARTIAL / FAILED
     private Integer billId;
     private Integer cardId;
     private BigDecimal plannedPay;    // 使用者原本想付
     private Integer plannedRedeem;    // 使用者原本想折抵
     private BigDecimal actualPay;     // 實際付款金額
     private Integer actualRedeem;     // 實際折抵點數
     private Integer remainPoints;     // 扣完的剩餘點數
     private BigDecimal outstandingAfter; // 預估未繳
     private String message;
}
