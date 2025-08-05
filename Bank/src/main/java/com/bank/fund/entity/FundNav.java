package com.bank.fund.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "fund_nav", uniqueConstraints = @UniqueConstraint(columnNames = { "fund_id", "nav_date" }))
public class FundNav {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "nav_id")
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "fund_id", nullable = false)
	private Fund fund;

	@Column(name = "nav_date", nullable = false)
	private LocalDate navDate;

	@Column(name = "nav", precision = 18, scale = 4, nullable = false)
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

	public FundNav(Integer id, Fund fund, LocalDate navDate, BigDecimal nav) {
		super();
		this.id = id;
		this.fund = fund;
		this.navDate = navDate;
		this.nav = nav;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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
