package com.bank.account.bean;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity @Table(name="transactions")
//@Component
public class Transactions {

	@Id @Column(name="transaction_id")
	private String transactionId;
	
	@Column(name="account_id")
	private String accountId;
	
	@Column(name="transaction_type")
	private String transactionType;
	
	@Column(name="to_bank_code")
	private String toBankCode;
	
	@Column(name="to_account_id")
	private String toAccountId;
	
	@Column(name="currency")
	private String currency;
	
	@Column(name="amount")
	private BigDecimal amount;
	
	@Column(name="tx_time")
	private LocalDateTime txTime;
	
	@Column(name="memo")
	private String memo;
	
	@Column(name="status")
	private String status;
	
	@Column(name="operator_id")
	private Integer operatorId;
	
	@Column(name="balance_after")
	private BigDecimal balanceAfter; 

	public Transactions() {
		super();
	}

	public Transactions(String transactionId, String accountId, String transactionType, String toBankCode,
			String toAccountId, String currency, BigDecimal amount, LocalDateTime txTime, String memo, String status,
			Integer operatorId, BigDecimal balanceAfter) {
		super();
		this.transactionId = transactionId;
		this.accountId = accountId;
		this.transactionType = transactionType;
		this.toBankCode = toBankCode;
		this.toAccountId = toAccountId;
		this.currency = currency;
		this.amount = amount;
		this.txTime = txTime;
		this.memo = memo;
		this.status = status;
		this.operatorId = operatorId;
		this.balanceAfter = balanceAfter;
	}

	public Transactions(String accountId, String transactionType, String toBankCode, String toAccountId,
			String currency, BigDecimal amount, LocalDateTime txTime, String memo, String status, Integer operatorId,
			BigDecimal balanceAfter) {
		super();
		this.accountId = accountId;
		this.transactionType = transactionType;
		this.toBankCode = toBankCode;
		this.toAccountId = toAccountId;
		this.currency = currency;
		this.amount = amount;
		this.txTime = txTime;
		this.memo = memo;
		this.status = status;
		this.operatorId = operatorId;
		this.balanceAfter = balanceAfter;
	}

	public Transactions(String transactionId, String accountId, String transactionType, String currency,
			BigDecimal amount, LocalDateTime txTime, String memo, String status, Integer operatorId,
			BigDecimal balanceAfter) {
		super();
		this.transactionId = transactionId;
		this.accountId = accountId;
		this.transactionType = transactionType;
		this.currency = currency;
		this.amount = amount;
		this.txTime = txTime;
		this.memo = memo;
		this.status = status;
		this.operatorId = operatorId;
		this.balanceAfter = balanceAfter;
	}

	

	public BigDecimal getBalanceAfter() {
		return balanceAfter;
	}

	public void setBalanceAfter(BigDecimal balanceAfter) {
		this.balanceAfter = balanceAfter;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public String getToBankCode() {
		return toBankCode;
	}

	public void setToBankCode(String toBankCode) {
		this.toBankCode = toBankCode;
	}

	public String getToAccountId() {
		return toAccountId;
	}

	public void setToAccountId(String toAccountId) {
		this.toAccountId = toAccountId;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public LocalDateTime getTxTime() {
		return txTime;
	}

	public void setTxTime(LocalDateTime txTime) {
		this.txTime = txTime;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(Integer operatorId) {
		this.operatorId = operatorId;
	}
	
	

}