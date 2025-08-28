package com.bank.fund.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

import com.bank.account.bean.Account;

@Entity
@Table(name = "fund")
public class Fund {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fund_id")
    private Integer fundId;
    
    @Column(name = "fund_code", unique = true, nullable = false, length = 20)
    private String fundCode;
    
    @Column(name = "fund_name", nullable = false, length = 100)
    private String fundName;
    
    @Column(name = "fund_type", length = 50)
    private String fundType;
    
    @Column(name = "risk_level")
    private Integer riskLevel;
    
    @Column(name = "currency", length = 10)
    private String currency;
    
    @Column(name = "size", precision = 18, scale = 2)
    private BigDecimal size;
    
    @Column(name = "min_buy", precision = 18, scale = 2)
    private BigDecimal minBuy;
    
    @Column(name = "buy_fee", precision = 5, scale = 2)
    private BigDecimal buyFee;
    
    @Column(name = "status", length = 20)
    private String status;
    
    @Column(name = "launch_time")
    private LocalDateTime launchTime;
    
    @OneToMany(mappedBy = "fund", fetch = FetchType.LAZY)
    @OrderBy("navDate DESC")
    private List<FundNav> fundNavs;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "com_acc_id", referencedColumnName = "account_id")
    private Account account;
    
    // 建構子
    public Fund() {
        super();
    }
    
    public Fund(String fundCode, String fundName, String fundType, Integer riskLevel, 
                String currency, BigDecimal size, BigDecimal minBuy, BigDecimal buyFee, 
                String status, LocalDateTime launchTime, Account account) {
        this.fundCode = fundCode;
        this.fundName = fundName;
        this.fundType = fundType;
        this.riskLevel = riskLevel;
        this.currency = currency;
        this.size = size;
        this.minBuy = minBuy;
        this.buyFee = buyFee;
        this.status = status;
        this.launchTime = launchTime;
        this.account = account;
    }
    
    // Getter 和 Setter
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
    
    public String getCurrency() {
        return currency;
    }
    
    public void setCurrency(String currency) {
        this.currency = currency;
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
    
    public Account getAccount() {
        return account;
    }
    
    public void setAccount(Account account) {
        this.account = account;
    }
    public List<FundNav> getFundNavs() {
        return fundNavs;
    }

    public void setFundNavs(List<FundNav> fundNavs) {
        this.fundNavs = fundNavs;
    }
    // 計算屬性方法
    @Transient
    public BigDecimal getLatestNav() {
        if (fundNavs != null && !fundNavs.isEmpty()) {
            // 確保按日期排序
            return fundNavs.stream()
                    .max(Comparator.comparing(FundNav::getNavDate))
                    .map(FundNav::getNav)
                    .orElse(null);
        }
        return null;
    }

    @Transient
    public LocalDate getLatestNavDate() {
        if (fundNavs != null && !fundNavs.isEmpty()) {
            return fundNavs.stream()
                    .max(Comparator.comparing(FundNav::getNavDate))
                    .map(FundNav::getNavDate)
                    .orElse(null);
        }
        return null;
    }
    @Override
    public String toString() {
        return "Fund{" +
                "fundId=" + fundId +
                ", fundCode='" + fundCode + '\'' +
                ", fundName='" + fundName + '\'' +
                ", fundType='" + fundType + '\'' +
                ", riskLevel=" + riskLevel +
                ", currency='" + currency + '\'' +
                ", size=" + size +
                ", minBuy=" + minBuy +
                ", buyFee=" + buyFee +
                ", status='" + status + '\'' +
                ", launchTime=" + launchTime +
                '}';
    }
}