package com.bank.fund.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.bank.account.bean.Transactions;

@Entity
@Table(name = "fund_transaction")
public class FundTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer fundTranId;

    @OneToOne
    @JoinColumn(name = "transaction_id", nullable = false)
    private Transactions transactions;

    @ManyToOne
    @JoinColumn(name = "fund_acc_id", nullable = false)
    private FundAccount fundAccount;

    @ManyToOne
    @JoinColumn(name = "fund_id", nullable = false)
    private Fund fund;

    @Column(name = "tran_type", length = 10, nullable = false)
    private String tranType;

    @Column(precision = 18, scale = 2)
    private BigDecimal amount;

    @Column(precision = 18, scale = 2)
    private BigDecimal fee;

    @Column(precision = 18, scale = 4)
    private BigDecimal units;

    @Column(precision = 18, scale = 4)
    private BigDecimal nav;

    @Column(name = "tran_time", nullable = false)
    private LocalDateTime tranTime = LocalDateTime.now();

    @Column(length = 10, nullable = false)
    private String status = "待交易";

    @Column(length = 200)
    private String memo;

	public FundTransaction() {
		super();
		// TODO Auto-generated constructor stub
	}

	public FundTransaction(Transactions transactions, FundAccount fundAccount, Fund fund, String tranType,
			BigDecimal amount, BigDecimal fee, BigDecimal units, BigDecimal nav, LocalDateTime tranTime, String status,
			String memo) {
		super();
		this.transactions = transactions;
		this.fundAccount = fundAccount;
		this.fund = fund;
		this.tranType = tranType;
		this.amount = amount;
		this.fee = fee;
		this.units = units;
		this.nav = nav;
		this.tranTime = tranTime;
		this.status = status;
		this.memo = memo;
	}

	public FundTransaction(Integer fundTranId, Transactions transactions, FundAccount fundAccount, Fund fund,
			String tranType, BigDecimal amount, BigDecimal fee, BigDecimal units, BigDecimal nav,
			LocalDateTime tranTime, String status, String memo) {
		super();
		this.fundTranId = fundTranId;
		this.transactions = transactions;
		this.fundAccount = fundAccount;
		this.fund = fund;
		this.tranType = tranType;
		this.amount = amount;
		this.fee = fee;
		this.units = units;
		this.nav = nav;
		this.tranTime = tranTime;
		this.status = status;
		this.memo = memo;
	}

	public Integer getFundTranId() {
		return fundTranId;
	}

	public void setFundTranId(Integer fundTranId) {
		this.fundTranId = fundTranId;
	}

	public Transactions getTransactions() {
		return transactions;
	}

	public void setTransactions(Transactions transactions) {
		this.transactions = transactions;
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

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getFee() {
		return fee;
	}

	public void setFee(BigDecimal fee) {
		this.fee = fee;
	}

	public BigDecimal getUnits() {
		return units;
	}

	public void setUnits(BigDecimal units) {
		this.units = units;
	}

	public BigDecimal getNav() {
		return nav;
	}

	public void setNav(BigDecimal nav) {
		this.nav = nav;
	}

	public LocalDateTime getTranTime() {
		return tranTime;
	}

	public void setTranTime(LocalDateTime tranTime) {
		this.tranTime = tranTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}
}
