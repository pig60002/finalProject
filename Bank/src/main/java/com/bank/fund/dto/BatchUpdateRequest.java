package com.bank.fund.dto;

import java.time.LocalDate;
import java.util.List;

/**
 * 批量更新請求DTO
 */
public class BatchUpdateRequest {
    private List<String> fundCodes;
    private LocalDate navDate;
    
    // 建構子
    public BatchUpdateRequest() {}
    
    public BatchUpdateRequest(List<String> fundCodes, LocalDate navDate) {
        this.fundCodes = fundCodes;
        this.navDate = navDate;
    }
    
    // Getters and Setters
    public List<String> getFundCodes() {
        return fundCodes;
    }
    
    public void setFundCodes(List<String> fundCodes) {
        this.fundCodes = fundCodes;
    }
    
    public LocalDate getNavDate() {
        return navDate;
    }
    
    public void setNavDate(LocalDate navDate) {
        this.navDate = navDate;
    }
    
    @Override
    public String toString() {
        return "BatchUpdateRequest{" +
                "fundCodes=" + fundCodes +
                ", navDate=" + navDate +
                '}';
    }
}