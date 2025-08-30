package com.bank.fund.dto;

import java.math.BigDecimal;

/**
 * 基金資料傳輸物件
 * 用於在 Repository 查詢時傳輸基金的基本資訊
 */
public class FundDto {
    
    private Integer fundId;
    private String fundCode;
    private String fundName;
    private BigDecimal latestNav;
    private Integer riskLevel;
    
    // 無參數建構子
    public FundDto() {
    }
    
    // 全參數建構子（用於 JPA 查詢）
    public FundDto(Integer fundId, String fundCode, String fundName, 
                   BigDecimal latestNav, Integer riskLevel) {
        this.fundId = fundId;
        this.fundCode = fundCode;
        this.fundName = fundName;
        this.latestNav = latestNav;
        this.riskLevel = riskLevel;
    }
    
    // 部分參數建構子
    public FundDto(Integer fundId, String fundCode, String fundName) {
        this.fundId = fundId;
        this.fundCode = fundCode;
        this.fundName = fundName;
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
    
    public BigDecimal getLatestNav() {
        return latestNav;
    }
    
    public void setLatestNav(BigDecimal latestNav) {
        this.latestNav = latestNav;
    }
    
    public Integer getRiskLevel() {
        return riskLevel;
    }
    
    public void setRiskLevel(Integer riskLevel) {
        this.riskLevel = riskLevel;
    }
    
    @Override
    public String toString() {
        return "FundDto{" +
                "fundId=" + fundId +
                ", fundCode='" + fundCode + '\'' +
                ", fundName='" + fundName + '\'' +
                ", latestNav=" + latestNav +
                ", riskLevel=" + riskLevel +
                '}';
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        FundDto fundDto = (FundDto) o;
        
        return fundId != null ? fundId.equals(fundDto.fundId) : fundDto.fundId == null;
    }
    
    @Override
    public int hashCode() {
        return fundId != null ? fundId.hashCode() : 0;
    }
}