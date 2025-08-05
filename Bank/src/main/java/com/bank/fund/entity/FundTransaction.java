package com.bank.fund.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "fund_transaction")
public class FundTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fund_tran_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "fund_acc_id", nullable = false)
    private FundAccount fundAccount;

    @ManyToOne
    @JoinColumn(name = "fund_id", nullable = false)
    private Fund fund;

    @Column(name = "tran_type", length = 2, nullable = false)
    private String tranType;

    @Column(name = "units", precision = 18, scale = 4, nullable = false)
    private BigDecimal units;

    @Column(name = "price", precision = 18, scale = 4, nullable = false)
    private BigDecimal price;

    @Column(name = "fee", precision = 18, scale = 4, nullable = false)
    private BigDecimal fee;

    @Column(name = "balance", precision = 18, scale = 4, nullable = false)
    private BigDecimal balance;

    @Column(name = "tran_time", nullable = false)
    private LocalDateTime tranTime;

    @Column(name = "tran_status", length = 3, nullable = false)
    private String tranStatus;

    @Column(name = "remark", length = 200)
    private String remark;

	public FundTransaction() {
		super();
		// TODO Auto-generated constructor stub
	}

	public FundTransaction(FundAccount fundAccount, Fund fund, String tranType, BigDecimal units, BigDecimal price,
			BigDecimal fee, BigDecimal balance, LocalDateTime tranTime, String tranStatus, String remark) {
		super();
		this.fundAccount = fundAccount;
		this.fund = fund;
		this.tranType = tranType;
		this.units = units;
		this.price = price;
		this.fee = fee;
		this.balance = balance;
		this.tranTime = tranTime;
		this.tranStatus = tranStatus;
		this.remark = remark;
	}

	public FundTransaction(Integer id, FundAccount fundAccount, Fund fund, String tranType, BigDecimal units,
			BigDecimal price, BigDecimal fee, BigDecimal balance, LocalDateTime tranTime, String tranStatus,
			String remark) {
		super();
		this.id = id;
		this.fundAccount = fundAccount;
		this.fund = fund;
		this.tranType = tranType;
		this.units = units;
		this.price = price;
		this.fee = fee;
		this.balance = balance;
		this.tranTime = tranTime;
		this.tranStatus = tranStatus;
		this.remark = remark;
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

	public String getTranType() {
		return tranType;
	}

	public void setTranType(String tranType) {
		this.tranType = tranType;
	}

	public BigDecimal getUnits() {
		return units;
	}

	public void setUnits(BigDecimal units) {
		this.units = units;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public BigDecimal getFee() {
		return fee;
	}

	public void setFee(BigDecimal fee) {
		this.fee = fee;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public LocalDateTime getTranTime() {
		return tranTime;
	}

	public void setTranTime(LocalDateTime tranTime) {
		this.tranTime = tranTime;
	}

	public String getTranStatus() {
		return tranStatus;
	}

	public void setTranStatus(String tranStatus) {
		this.tranStatus = tranStatus;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

    
}
