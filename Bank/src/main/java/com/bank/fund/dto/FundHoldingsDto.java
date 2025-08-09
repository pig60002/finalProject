package com.bank.fund.dto;

import java.math.BigDecimal;

public class FundHoldingsDto {
	
	private Integer id;
	private String fundCode;
	private String fundName;
	private BigDecimal units;
	private BigDecimal cost;
	
	public FundHoldingsDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public FundHoldingsDto(String fundCode, String fundName, BigDecimal units, BigDecimal cost) {
		super();
		this.fundCode = fundCode;
		this.fundName = fundName;
		this.units = units;
		this.cost = cost;
	}

	public FundHoldingsDto(Integer id, String fundCode, String fundName, BigDecimal units, BigDecimal cost) {
		super();
		this.id = id;
		this.fundCode = fundCode;
		this.fundName = fundName;
		this.units = units;
		this.cost = cost;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFundCode() {
		return fundCode;
	}

	public void setFundCode(String fundCode) {
		this.fundCode = fundCode;
	}

	public String getFundName() {
		return fundName;
	}

	public void setFundName(String fundName) {
		this.fundName = fundName;
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