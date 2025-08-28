package com.bank.fund.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "fund_nav")
public class FundNav {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "nav_id")
    private Integer navId;
    
    @Column(name = "fund_id")
    private Integer fundId;
    
    @Column(name = "nav", precision = 18, scale = 4)
    private BigDecimal nav;
    
    @Column(name = "nav_date")
    private LocalDate navDate;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fund_id", insertable = false, updatable = false)
    private Fund fund;
    
    // 建構子
    public FundNav() {}
    
    public FundNav(Integer fundId, BigDecimal nav, LocalDate navDate) {
        this.fundId = fundId;
        this.nav = nav;
        this.navDate = navDate;
    }
    
    // Getter 和 Setter
    public Integer getNavId() { return navId; }
    public void setNavId(Integer navId) { this.navId = navId; }
    
    public Integer getFundId() { return fundId; }
    public void setFundId(Integer fundId) { this.fundId = fundId; }
    
    public BigDecimal getNav() { return nav; }
    public void setNav(BigDecimal nav) { this.nav = nav; }
    
    public LocalDate getNavDate() { return navDate; }
    public void setNavDate(LocalDate navDate) { this.navDate = navDate; }
    
    public Fund getFund() { return fund; }
    public void setFund(Fund fund) { this.fund = fund; }
}