package com.bank.fund.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import com.bank.account.bean.Account;
import com.bank.member.bean.Member;

@Entity
@Table(name = "fund_account",
       uniqueConstraints = {
           @UniqueConstraint(columnNames = {"m_id"}),
           @UniqueConstraint(columnNames = {"account_id"})
       })
public class FundAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer fundAccId;

    @Column(name = "risk_type", length = 10, nullable = false)
    private String riskType;

    @Column(length = 10, nullable = false)
    private String status = "審核中";

    @Column(name = "open_time", nullable = false)
    private LocalDateTime openTime = LocalDateTime.now();

    // 會員
    @OneToOne
    @JoinColumn(name = "m_id", nullable = false, unique = true)
    private Member member;

    // 存款帳號
    @OneToOne
    @JoinColumn(name = "account_id", nullable = false, unique = true)
    private Account account;

	public FundAccount() {
		super();
		// TODO Auto-generated constructor stub
	}

	public FundAccount(String riskType, String status, LocalDateTime openTime, Member member, Account account) {
		super();
		this.riskType = riskType;
		this.status = status;
		this.openTime = openTime;
		this.member = member;
		this.account = account;
	}

	public FundAccount(Integer fundAccId, String riskType, String status, LocalDateTime openTime, Member member,
			Account account) {
		super();
		this.fundAccId = fundAccId;
		this.riskType = riskType;
		this.status = status;
		this.openTime = openTime;
		this.member = member;
		this.account = account;
	}

	public Integer getFundAccId() {
		return fundAccId;
	}

	public void setFundAccId(Integer fundAccId) {
		this.fundAccId = fundAccId;
	}

	public String getRiskType() {
		return riskType;
	}

	public void setRiskType(String riskType) {
		this.riskType = riskType;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public LocalDateTime getOpenTime() {
		return openTime;
	}

	public void setOpenTime(LocalDateTime openTime) {
		this.openTime = openTime;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}
}