package com.bank.account.bean;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity @Table(name="accounts")
//@Component
public class Account {

	@Id @Column(name="account_id")
	private String accountId;
	
	@Column(name="m_id")
	private Integer mId;
	
	@Column(name="account_name", nullable = false)
	private String accountName;
	
	@Column(name="currency")
	private String currency;
	
	@Column(name="balance")
	private BigDecimal balance;
	
	@Column(name="opened_date")
	private LocalDate openedDate;
	
	@Column(name="status")
	private String status;
	
	@Column(name="memo")
	private String memo;
	
	@Column(name="operator_id")
	private Integer operatorId;
	
	@Column(name="status_updated_time")
	private LocalDateTime statusUpdatedTime;
	
	public Account() {
		super();
	}

	public Account(String accountId, Integer mId, String accountName, String currency, BigDecimal balance,
			LocalDate openedDate, String status, String memo, Integer operatorId, LocalDateTime statusUpdatedTime) {
		super();
		this.accountId = accountId;
		this.mId = mId;
		this.accountName = accountName;
		this.currency = currency;
		this.balance = balance;
		this.openedDate = openedDate;
		this.status = status;
		this.memo = memo;
		this.operatorId = operatorId;
		this.statusUpdatedTime = statusUpdatedTime;
	}

	public Account(Integer mId, String accountName, String currency, BigDecimal balance, LocalDate openedDate,
			String status, String memo, Integer operatorId, LocalDateTime statusUpdatedTime) {
		super();
		this.mId = mId;
		this.accountName = accountName;
		this.currency = currency;
		this.balance = balance;
		this.openedDate = openedDate;
		this.status = status;
		this.memo = memo;
		this.operatorId = operatorId;
		this.statusUpdatedTime = statusUpdatedTime;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public Integer getmId() {
		return mId;
	}

	public void setmId(Integer mId) {
		this.mId = mId;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public LocalDate getOpenedDate() {
		return openedDate;
	}

	public void setOpenedDate(LocalDate openedDate) {
		this.openedDate = openedDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public Integer getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(Integer operatorId) {
		this.operatorId = operatorId;
	}

	public LocalDateTime getStatusUpdatedTime() {
		return statusUpdatedTime;
	}

	public void setStatusUpdatedTime(LocalDateTime statusUpdatedTime) {
		this.statusUpdatedTime = statusUpdatedTime;
	}

}
