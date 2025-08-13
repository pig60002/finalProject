package com.bank.fund.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "fund_holdings",
       uniqueConstraints = @UniqueConstraint(columnNames = {"fund_acc_id", "fund_id"}))
public class FundHoldings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer holdingId;

    @ManyToOne
    @JoinColumn(name = "fund_acc_id", nullable = false)
    private FundAccount fundAccount;

    @ManyToOne
    @JoinColumn(name = "fund_id", nullable = false)
    private Fund fund;

    @Column(nullable = false, precision = 18, scale = 4)
    private BigDecimal units;

    @Column(nullable = false, precision = 18, scale = 4)
    private BigDecimal cost;

    @Column(name = "update_time", nullable = false)
    private LocalDateTime updateTime = LocalDateTime.now();

	public FundHoldings() {
		super();
		// TODO Auto-generated constructor stub
	}

	public FundHoldings(FundAccount fundAccount, Fund fund, BigDecimal units, BigDecimal cost,
			LocalDateTime updateTime) {
		super();
		this.fundAccount = fundAccount;
		this.fund = fund;
		this.units = units;
		this.cost = cost;
		this.updateTime = updateTime;
	}

	public FundHoldings(Integer holdingId, FundAccount fundAccount, Fund fund, BigDecimal units, BigDecimal cost,
			LocalDateTime updateTime) {
		super();
		this.holdingId = holdingId;
		this.fundAccount = fundAccount;
		this.fund = fund;
		this.units = units;
		this.cost = cost;
		this.updateTime = updateTime;
	}

	public Integer getHoldingId() {
		return holdingId;
	}

	public void setHoldingId(Integer holdingId) {
		this.holdingId = holdingId;
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

	public BigDecimal getUnits() {
		return units;
	}

	public void setUnits(BigDecimal units) {
		this.units = units;
	}

	public BigDecimal getCost() {
		return cost;
	}

	public void setCost(BigDecimal cost) {
		this.cost = cost;
	}

	public LocalDateTime getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(LocalDateTime updateTime) {
		this.updateTime = updateTime;
	}
}
