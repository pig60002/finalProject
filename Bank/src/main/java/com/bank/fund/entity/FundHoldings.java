package com.bank.fund.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "fund_holdings", uniqueConstraints = @UniqueConstraint(columnNames = { "fund_acc_id", "fund_id" }))
public class FundHoldings {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "fund_hold_id")
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "fund_acc_id", nullable = false)
	private FundAccount fundAccount;

	@ManyToOne
	@JoinColumn(name = "fund_id", nullable = false)
	private Fund fund;

	@Column(name = "units", precision = 18, scale = 4, nullable = false)
	private BigDecimal units;

	@Column(name = "cost", precision = 18, scale = 4, nullable = false)
	private BigDecimal cost;

	public FundHoldings() {
		super();
		// TODO Auto-generated constructor stub
	}

	public FundHoldings(FundAccount fundAccount, Fund fund, BigDecimal units, BigDecimal cost) {
		super();
		this.fundAccount = fundAccount;
		this.fund = fund;
		this.units = units;
		this.cost = cost;
	}

	public FundHoldings(Integer id, FundAccount fundAccount, Fund fund, BigDecimal units, BigDecimal cost) {
		super();
		this.id = id;
		this.fundAccount = fundAccount;
		this.fund = fund;
		this.units = units;
		this.cost = cost;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

}
