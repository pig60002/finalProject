package com.bank.fund.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "fund_nav",
       uniqueConstraints = @UniqueConstraint(columnNames = {"fund_id", "nav_date"}))
public class FundNav {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer navId;

    @ManyToOne
    @JoinColumn(name = "fund_id", nullable = false)
    private Fund fund;

    @Column(name = "nav_date", nullable = false)
    private LocalDate navDate;

    @Column(nullable = false, precision = 18, scale = 4)
    private BigDecimal nav;

	public FundNav() {
		super();
		// TODO Auto-generated constructor stub
	}

	public FundNav(Fund fund, LocalDate navDate, BigDecimal nav) {
		super();
		this.fund = fund;
		this.navDate = navDate;
		this.nav = nav;
	}

	public FundNav(Integer navId, Fund fund, LocalDate navDate, BigDecimal nav) {
		super();
		this.navId = navId;
		this.fund = fund;
		this.navDate = navDate;
		this.nav = nav;
	}

	public Integer getNavId() {
		return navId;
	}

	public void setNavId(Integer navId) {
		this.navId = navId;
	}

	public Fund getFund() {
		return fund;
	}

	public void setFund(Fund fund) {
		this.fund = fund;
	}

	public LocalDate getNavDate() {
		return navDate;
	}

	public void setNavDate(LocalDate navDate) {
		this.navDate = navDate;
	}

	public BigDecimal getNav() {
		return nav;
	}

	public void setNav(BigDecimal nav) {
		this.nav = nav;
	}
}
