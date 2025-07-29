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
	private LocalDateTime tx_time;
	
	@Column(name="memo")
	private String memo;
	
	@Column(name="status")
	private String status;
	
	@Column(name="operator_id")
	private Integer operatorId;

	public Transactions() {
		super();
	}

	public Transactions(String transactionId, String accountId, String transactionType, String toBankCode,
			String toAccountId, String currency, BigDecimal amount, LocalDateTime tx_time, String memo, String status,
			Integer operatorId) {
		super();
		this.transactionId = transactionId;
		this.accountId = accountId;
		this.transactionType = transactionType;
		this.toBankCode = toBankCode;
		this.toAccountId = toAccountId;
		this.currency = currency;
		this.amount = amount;
		this.tx_time = tx_time;
		this.memo = memo;
		this.status = status;
		this.operatorId = operatorId;
	}

	public Transactions(String accountId, String transactionType, String toBankCode, String toAccountId,
			String currency, BigDecimal amount, LocalDateTime tx_time, String memo, String status, Integer operatorId) {
		super();
		this.accountId = accountId;
		this.transactionType = transactionType;
		this.toBankCode = toBankCode;
		this.toAccountId = toAccountId;
		this.currency = currency;
		this.amount = amount;
		this.tx_time = tx_time;
		this.memo = memo;
		this.status = status;
		this.operatorId = operatorId;
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

	public LocalDateTime getTx_time() {
		return tx_time;
	}

	public void setTx_time(LocalDateTime tx_time) {
		this.tx_time = tx_time;
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