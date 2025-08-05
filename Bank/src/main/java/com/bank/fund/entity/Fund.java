package com.bank.fund.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "fund")
public class Fund {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fund_id")
    private Integer id;

    @Column(name = "fund_code", length = 20, nullable = false, unique = true)
    private String fundCode;

    @Column(name = "fund_name", length = 100, nullable = false)
    private String fundName;

    @Column(name = "currency", length = 3)
    private String currency;

    @Column(name = "fund_type", length = 20)
    private String type;

    @Column(name = "size", precision = 18, scale = 4)
    private BigDecimal size;

    @Column(name = "create_date")
    private LocalDate createDate;

    @Column(name = "invest_area", length = 20)
    private String investArea;

    @Column(name = "min_buy", precision = 18, scale = 4)
    private BigDecimal minBuy;

    @Column(name = "buy_fee", precision = 18, scale = 4)
    private BigDecimal buyFee;

    @Column(name = "risk_level")
    private Integer riskLevel;

    @Column(name = "fund_status", length = 3)
    private String status;
    

	public Fund() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Fund(String fundCode, String fundName, String currency, String type, BigDecimal size, LocalDate createDate,
			String investArea, BigDecimal minBuy, BigDecimal buyFee, Integer riskLevel, String status) {
		super();
		this.fundCode = fundCode;
		this.fundName = fundName;
		this.currency = currency;
		this.type = type;
		this.size = size;
		this.createDate = createDate;
		this.investArea = investArea;
		this.minBuy = minBuy;
		this.buyFee = buyFee;
		this.riskLevel = riskLevel;
		this.status = status;
	}

	public Fund(Integer id, String fundCode, String fundName, String currency, String type, BigDecimal size,
			LocalDate createDate, String investArea, BigDecimal minBuy, BigDecimal buyFee, Integer riskLevel,
			String status) {
		super();
		this.id = id;
		this.fundCode = fundCode;
		this.fundName = fundName;
		this.currency = currency;
		this.type = type;
		this.size = size;
		this.createDate = createDate;
		this.investArea = investArea;
		this.minBuy = minBuy;
		this.buyFee = buyFee;
		this.riskLevel = riskLevel;
		this.status = status;
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

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public BigDecimal getSize() {
		return size;
	}

	public void setSize(BigDecimal size) {
		this.size = size;
	}

	public LocalDate getCreateDate() {
		return createDate;
	}

	public void setCreateDate(LocalDate createDate) {
		this.createDate = createDate;
	}

	public String getInvestArea() {
		return investArea;
	}

	public void setInvestArea(String investArea) {
		this.investArea = investArea;
	}

	public BigDecimal getMinBuy() {
		return minBuy;
	}

	public void setMinBuy(BigDecimal minBuy) {
		this.minBuy = minBuy;
	}

	public BigDecimal getBuyFee() {
		return buyFee;
	}

	public void setBuyFee(BigDecimal buyFee) {
		this.buyFee = buyFee;
	}

	public Integer getRiskLevel() {
		return riskLevel;
	}

	public void setRiskLevel(Integer riskLevel) {
		this.riskLevel = riskLevel;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

    
}
