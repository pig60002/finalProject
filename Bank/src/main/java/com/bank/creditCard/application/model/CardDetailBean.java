package com.bank.creditCard.application.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class CardDetailBean {
	private int cardId;
	private int cardUserId;
	private int cardType;
	private String cvv;
	private LocalDate expDate;
	private BigDecimal creditLimit;
	private BigDecimal currentBal;
	private String status;
	private LocalDate issuedDate;
	public CardDetailBean() {
		super();
		// TODO Auto-generated constructor stub
	}
	public int getCardId() {
		return cardId;
	}
	public void setCardId(int cardId) {
		this.cardId = cardId;
	}
	public int getCardUserId() {
		return cardUserId;
	}
	public void setCardUserId(int cardUserId) {
		this.cardUserId = cardUserId;
	}
	public int getCardType() {
		return cardType;
	}
	public void setCardType(int cardType) {
		this.cardType = cardType;
	}
	public String getCvv() {
		return cvv;
	}
	public void setCvv(String cvv) {
		this.cvv = cvv;
	}
	public LocalDate getExpDate() {
		return expDate;
	}
	public void setExpDate(LocalDate expDate) {
		this.expDate = expDate;
	}
	public BigDecimal getCreditLimit() {
		return creditLimit;
	}
	public void setCreditLimit(BigDecimal creditLimit) {
		this.creditLimit = creditLimit;
	}
	public BigDecimal getCurrentBal() {
		return currentBal;
	}
	public void setCurrentBal(BigDecimal currentBal) {
		this.currentBal = currentBal;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public LocalDate getIssuedDate() {
		return issuedDate;
	}
	public void setIssuedDate(LocalDate issuedDate) {
		this.issuedDate = issuedDate;
	}
	
	
}
