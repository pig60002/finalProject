package com.bank.fund.entity;

import com.bank.account.bean.Account;
import com.bank.member.bean.Member;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "fund_account")
public class FundAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fund_acc_id")
    private Integer id;

    @OneToOne
    @JoinColumn(name = "member_id", nullable = false, unique = true)
    private Member member;

    @OneToOne
    @JoinColumn(name = "account_id", nullable = false, unique = true)
    private Account account;

    @Column(name = "risk_type", length = 3, nullable = false)
    private String riskType;

    @Column(name = "acc_status", length = 3, nullable = false)
    private String status;

	public FundAccount() {
		super();
		// TODO Auto-generated constructor stub
	}

	public FundAccount(Member member, Account account, String riskType, String status) {
		super();
		this.member = member;
		this.account = account;
		this.riskType = riskType;
		this.status = status;
	}

	public FundAccount(Integer id, Member member, Account account, String riskType, String status) {
		super();
		this.id = id;
		this.member = member;
		this.account = account;
		this.riskType = riskType;
		this.status = status;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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
	
    
}
