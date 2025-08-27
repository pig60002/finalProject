package com.bank.fund.service;

import com.bank.fund.dto.FundHoldingsDto;
import com.bank.fund.repository.FundHoldingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FundHoldingsService {

    @Autowired
    private FundHoldingsRepository fundHoldingsRepository;

    /**
     * 根據基金帳戶ID查詢所有持有基金
     */
    public List<FundHoldingsDto> getHoldingsByFundAccId(Integer fundAccId) {
        List<Object[]> results = fundHoldingsRepository.findHoldingsByFundAccId(fundAccId);
        return results.stream()
            .map(this::convertToFundHoldingsDto)
            .collect(Collectors.toList());
    }

    /**
     * 根據條件查詢持有基金
     */
    public List<FundHoldingsDto> getHoldingsByConditions(Integer fundAccId, String fundType, 
                                                        Integer riskLevel, String status) {
        List<Object[]> results = fundHoldingsRepository.findHoldingsByConditions(
            fundAccId, fundType, riskLevel, status);
        return results.stream()
            .map(this::convertToFundHoldingsDto)
            .collect(Collectors.toList());
    }

    /**
     * 轉換Object[]到FundHoldingsDto
     */
    private FundHoldingsDto convertToFundHoldingsDto(Object[] row) {
        return new FundHoldingsDto(
            (Integer) row[0],           // holdingId
            (Integer) row[1],           // fundAccId
            (Integer) row[2],           // fundId
            (BigDecimal) row[3],        // units
            (BigDecimal) row[4],        // cost
            (String) row[5],            // fundCode
            (String) row[6],            // fundName
            (String) row[7],            // fund_type
            (Integer) row[8],           // risk_level
            (BigDecimal) row[9],        // buyFee
            (String) row[10],           // status
            (BigDecimal) row[11],       // latestNav
            row[12] != null ? ((java.sql.Date) row[12]).toLocalDate() : null  // navDate
        );
    }

    /**
     * 獲取持有基金統計資訊
     */
    public Object[] getHoldingsSummary(Integer fundAccId) {
        return fundHoldingsRepository.getHoldingsSummary(fundAccId);
    }
}