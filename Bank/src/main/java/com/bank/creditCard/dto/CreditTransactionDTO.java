package com.bank.creditCard.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class CreditTransactionDTO {
	
	private Integer transactionCode;
    private Integer cardId;
    private Integer memberId; // 持卡人會員ID，可選
    private Integer creditBillId;
    private BigDecimal amount;
    private String merchantType;
    private BigDecimal cashback;
    private String description;

}
