package com.bank.fund.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class FundDto {
	
	private Integer fundId;
	private String fundCode;
	private String fundName;
	private BigDecimal latestNav;
	private LocalDate navDate;
	private String fundType;
	private Integer riskLevel;
	private BigDecimal buyFee;
	private String status;
	
	public FundDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public FundDto(String fundCode, String fundName, BigDecimal latestNav, LocalDate navDate,
			String fundType, Integer riskLevel, BigDecimal buyFee, String status) {
		super();
		this.fundCode = fundCode;
		this.fundName = fundName;
		this.latestNav = latestNav;
		this.navDate = navDate;
		this.fundType = fundType;
		this.riskLevel = riskLevel;
		this.buyFee = buyFee;
		this.status = status;
	}

	public FundDto(Integer fundId, String fundCode, String fundName, BigDecimal latestNav, LocalDate navDate,
			 String fundType, Integer riskLevel, BigDecimal buyFee, String status) {
		super();
		this.fundId = fundId;
		this.fundCode = fundCode;
		this.fundName = fundName;
		this.latestNav = latestNav;
		this.navDate = navDate;
		this.fundType = fundType;
		this.riskLevel = riskLevel;
		this.buyFee = buyFee;
		this.status = status;
	}

	public Integer getFundId() {
		return fundId;
	}

	public void setFundId(Integer fundId) {
		this.fundId = fundId;
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

	public BigDecimal getLatestNav() {
		return latestNav;
	}

	public void setLatestNav(BigDecimal latestNav) {
		this.latestNav = latestNav;
	}

	public LocalDate getNavDate() {
		return navDate;
	}

	public void setNavDate(LocalDate navDate) {
		this.navDate = navDate;
	}

	public String getFundType() {
		return fundType;
	}

	public void setFundType(String fundType) {
		this.fundType = fundType;
	}

	public Integer getRiskLevel() {
		return riskLevel;
	}

	public void setRiskLevel(Integer riskLevel) {
		this.riskLevel = riskLevel;
	}

	public BigDecimal getBuyFee() {
		return buyFee;
	}

	public void setBuyFee(BigDecimal buyFee) {
		this.buyFee = buyFee;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	
}
