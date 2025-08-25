package com.bank.fund.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "fund_sip")
public class FundSip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer sipId;

    @ManyToOne
    @JoinColumn(name = "fund_acc_id", nullable = false)
    private FundAccount fundAccount;

    @ManyToOne
    @JoinColumn(name = "fund_id", nullable = false)
    private Fund fund;

    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal amount;

    @Column(length = 10, nullable = false)
    private String frequency;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(length = 10, nullable = false)
    private String status;

	public FundSip() {
		super();
		// TODO Auto-generated constructor stub
	}

	public FundSip(FundAccount fundAccount, Fund fund, BigDecimal amount, String frequency, LocalDate startDate,
			LocalDate endDate, String status) {
		super();
		this.fundAccount = fundAccount;
		this.fund = fund;
		this.amount = amount;
		this.frequency = frequency;
		this.startDate = startDate;
		this.endDate = endDate;
		this.status = status;
	}

	public FundSip(Integer sipId, FundAccount fundAccount, Fund fund, BigDecimal amount, String frequency,
			LocalDate startDate, LocalDate endDate, String status) {
		super();
		this.sipId = sipId;
		this.fundAccount = fundAccount;
		this.fund = fund;
		this.amount = amount;
		this.frequency = frequency;
		this.startDate = startDate;
		this.endDate = endDate;
		this.status = status;
	}

	public Integer getSipId() {
		return sipId;
	}

	public void setSipId(Integer sipId) {
		this.sipId = sipId;
	}

	public FundAccount getFundAccount() {
		return fundAccount;
	}

	public void setFundAccount(FundAccount fundAccount) {
		this.fundAccount = fundAccount;
	}

	public Fund getFund() {
		return fund;
	}

	public void setFund(Fund fund) {
		this.fund = fund;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getFrequency() {
		return frequency;
	}

	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
