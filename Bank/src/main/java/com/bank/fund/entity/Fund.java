package com.bank.fund.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import com.bank.account.bean.Account;
import java.math.BigDecimal;

@Entity
@Table(name = "fund", uniqueConstraints = @UniqueConstraint(columnNames = "fund_code"))
public class Fund {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer fundId;

    @Column(name = "fund_code", length = 20, nullable = false, unique = true)
    private String fundCode;

    @Column(name = "fund_name", length = 100, nullable = false)
    private String fundName;

    @ManyToOne
    @JoinColumn(name = "com_acc_id", nullable = false)
    private Account companyAccount;

    @Column(length = 10, nullable = false)
    private String currency;

    @Column(name = "fund_type", length = 20, nullable = false)
    private String fundType;

    @Column(name = "risk_level", nullable = false)
    private Integer riskLevel;

    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal size;

    @Column(name = "min_buy", nullable = false, precision = 18, scale = 2)
    private BigDecimal minBuy;

    @Column(name = "buy_fee", nullable = false, precision = 18, scale = 2)
    private BigDecimal buyFee;

    @Column(length = 10, nullable = false)
    private String status;

    @Column(name = "launch_time", nullable = false)
    private LocalDateTime launchTime;

	public Fund() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Fund(String fundCode, String fundName, Account companyAccount, String currency, String fundType,
			Integer riskLevel, BigDecimal size, BigDecimal minBuy, BigDecimal buyFee, String status,
			LocalDateTime launchTime) {
		super();
		this.fundCode = fundCode;
		this.fundName = fundName;
		this.companyAccount = companyAccount;
		this.currency = currency;
		this.fundType = fundType;
		this.riskLevel = riskLevel;
		this.size = size;
		this.minBuy = minBuy;
		this.buyFee = buyFee;
		this.status = status;
		this.launchTime = launchTime;
	}

	public Fund(Integer fundId, String fundCode, String fundName, Account companyAccount, String currency,
			String fundType, Integer riskLevel, BigDecimal size, BigDecimal minBuy, BigDecimal buyFee, String status,
			LocalDateTime launchTime) {
		super();
		this.fundId = fundId;
		this.fundCode = fundCode;
		this.fundName = fundName;
		this.companyAccount = companyAccount;
		this.currency = currency;
		this.fundType = fundType;
		this.riskLevel = riskLevel;
		this.size = size;
		this.minBuy = minBuy;
		this.buyFee = buyFee;
		this.status = status;
		this.launchTime = launchTime;
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

	public Account getCompanyAccount() {
		return companyAccount;
	}

	public void setCompanyAccount(Account companyAccount) {
		this.companyAccount = companyAccount;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
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

	public BigDecimal getSize() {
		return size;
	}

	public void setSize(BigDecimal size) {
		this.size = size;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public LocalDateTime getLaunchTime() {
		return launchTime;
	}

	public void setLaunchTime(LocalDateTime launchTime) {
		this.launchTime = launchTime;
	}
}
