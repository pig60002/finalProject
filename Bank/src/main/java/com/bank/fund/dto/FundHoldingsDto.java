package com.bank.fund.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class FundHoldingsDto {
	
	private Integer holdingId;
	private Integer fundAccId;
	private Integer fundId;
	private BigDecimal units;
	private BigDecimal cost;
	private String fundCode;
	private String fundName;
	private String fund_type;
	private Integer risk_level;
	private BigDecimal buyFee;
	private String status;
	private BigDecimal latestNav;
	private LocalDate navDate;
	
	
	public FundHoldingsDto() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public FundHoldingsDto(Integer holdingId, Integer fundAccId, Integer fundId, BigDecimal units, BigDecimal cost,
			String fundCode, String fundName, String fund_type, Integer risk_level, BigDecimal buyFee, String status,
			BigDecimal latestNav, LocalDate navDate) {
		super();
		this.holdingId = holdingId;
		this.fundAccId = fundAccId;
		this.fundId = fundId;
		this.units = units;
		this.cost = cost;
		this.fundCode = fundCode;
		this.fundName = fundName;
		this.fund_type = fund_type;
		this.risk_level = risk_level;
		this.buyFee = buyFee;
		this.status = status;
		this.latestNav = latestNav;
		this.navDate = navDate;
	}
	
	public Integer getHoldingId() {
		return holdingId;
	}
	public void setHoldingId(Integer holdingId) {
		this.holdingId = holdingId;
	}
	public Integer getFundAccId() {
		return fundAccId;
	}
	public void setFundAccId(Integer fundAccId) {
		this.fundAccId = fundAccId;
	}
	public Integer getFundId() {
		return fundId;
	}
	public void setFundId(Integer fundId) {
		this.fundId = fundId;
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
	public String getFund_type() {
		return fund_type;
	}
	public void setFund_type(String fund_type) {
		this.fund_type = fund_type;
	}
	public Integer getRisk_level() {
		return risk_level;
	}
	public void setRisk_level(Integer risk_level) {
		this.risk_level = risk_level;
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
}