package com.bank.fund.service;

import com.bank.fund.entity.Fund;
import com.bank.fund.entity.FundNav;
import com.bank.fund.repository.FundRepository;
import com.bank.fund.repository.FundNavRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;

@Service
public class FundNavUpdateService {
    
    private static final Logger logger = LoggerFactory.getLogger(FundNavUpdateService.class);
    
    @Autowired
    private FundRepository fundRepository;
    
    @Autowired
    private FundNavRepository fundNavRepository;
    
    private final Random random = new Random();
    
    /**
     * 手動批量更新所有基金淨值
     * @param navDate 淨值日期（如果為null則使用今天）
     * @return 更新結果統計
     */
    @Transactional
    public BatchUpdateResult batchUpdateAllFunds(LocalDate navDate) {
        if (navDate == null) {
            navDate = LocalDate.now();
        }
        
        logger.info("開始批量更新所有基金淨值，日期: {}", navDate);
        
        List<Fund> allFunds = fundRepository.findByStatus("OPEN");
        int totalCount = allFunds.size();
        int successCount = 0;
        int skippedCount = 0;
        int errorCount = 0;
        
        for (Fund fund : allFunds) {
            try {
                UpdateResult result = updateFundNav(fund, navDate);
                switch (result) {
                    case SUCCESS:
                        successCount++;
                        break;
                    case SKIPPED:
                        skippedCount++;
                        break;
                    case ERROR:
                        errorCount++;
                        break;
                }
            } catch (Exception e) {
                logger.error("批量更新基金 {} 時發生錯誤: {}", fund.getFundCode(), e.getMessage());
                errorCount++;
            }
        }
        
        BatchUpdateResult result = new BatchUpdateResult(
            totalCount, successCount, skippedCount, errorCount, navDate);
        
        logger.info("批量更新完成 - 總計: {}, 成功: {}, 跳過: {}, 錯誤: {}", 
                   totalCount, successCount, skippedCount, errorCount);
        
        return result;
    }
    
    /**
     * 更新指定基金列表的淨值
     * @param fundCodes 基金代碼列表
     * @param navDate 淨值日期
     * @return 更新結果統計
     */
    @Transactional
    public BatchUpdateResult batchUpdateSpecificFunds(List<String> fundCodes, LocalDate navDate) {
        if (navDate == null) {
            navDate = LocalDate.now();
        }
        
        logger.info("開始批量更新指定基金淨值，數量: {}, 日期: {}", fundCodes.size(), navDate);
        
        int totalCount = fundCodes.size();
        int successCount = 0;
        int skippedCount = 0;
        int errorCount = 0;
        
        for (String fundCode : fundCodes) {
            try {
                Fund fund = fundRepository.findFundByCode(fundCode);
                if (fund == null) {
                    logger.warn("找不到基金代碼: {}", fundCode);
                    errorCount++;
                    continue;
                }
                
                UpdateResult result = updateFundNav(fund, navDate);
                switch (result) {
                    case SUCCESS:
                        successCount++;
                        break;
                    case SKIPPED:
                        skippedCount++;
                        break;
                    case ERROR:
                        errorCount++;
                        break;
                }
            } catch (Exception e) {
                logger.error("更新基金 {} 時發生錯誤: {}", fundCode, e.getMessage());
                errorCount++;
            }
        }
        
        BatchUpdateResult result = new BatchUpdateResult(
            totalCount, successCount, skippedCount, errorCount, navDate);
        
        logger.info("指定基金批量更新完成 - 總計: {}, 成功: {}, 跳過: {}, 錯誤: {}", 
                   totalCount, successCount, skippedCount, errorCount);
        
        return result;
    }
    
    /**
     * 更新單一基金淨值的核心方法
     * @param fund 基金實體
     * @param navDate 淨值日期
     * @return 更新結果
     */
    private UpdateResult updateFundNav(Fund fund, LocalDate navDate) {
        // 檢查是否已存在該日期的淨值
        if (fundNavRepository.existsByFundIdAndNavDate(fund.getFundId(), navDate)) {
            logger.debug("基金 {} 在 {} 的淨值已存在，跳過更新", fund.getFundCode(), navDate);
            return UpdateResult.SKIPPED;
        }
        
        try {
            // 獲取新的淨值
            BigDecimal newNav = calculateNewNav(fund);
            
            // 創建新的淨值記錄
            FundNav fundNav = new FundNav();
            fundNav.setFundId(fund.getFundId());
            fundNav.setNav(newNav);
            fundNav.setNavDate(navDate);
            
            fundNavRepository.save(fundNav);
            
            logger.info("成功更新基金 {} 淨值: {} (日期: {})", 
                       fund.getFundCode(), newNav, navDate);
            
            return UpdateResult.SUCCESS;
        } catch (Exception e) {
            logger.error("更新基金 {} 淨值時發生錯誤: {}", fund.getFundCode(), e.getMessage());
            return UpdateResult.ERROR;
        }
    }
    
    /**
     * 計算新淨值的方法
     * @param fund 基金實體
     * @return 計算出的新淨值
     */
    private BigDecimal calculateNewNav(Fund fund) {
        // 獲取最新淨值
        BigDecimal latestNav = fund.getLatestNav();
        
        if (latestNav == null) {
            // 如果沒有歷史淨值，設定初始淨值為 1.0000
            return BigDecimal.ONE.setScale(4, RoundingMode.HALF_UP);
        }
        
        // 模擬淨值變動 (-3% 到 +3% 的隨機變動)
        double changePercent = (random.nextDouble() - 0.5) * 0.06; // -0.03 到 0.03
        
        // 根據基金風險等級調整變動幅度
        Integer riskLevel = fund.getRiskLevel();
        if (riskLevel != null && riskLevel > 0) {
            changePercent *= (riskLevel / 5.0); // 風險等級越高變動越大
        }
        
        BigDecimal changeAmount = latestNav.multiply(BigDecimal.valueOf(changePercent));
        BigDecimal newNav = latestNav.add(changeAmount);
        
        // 確保淨值不會變為負數或過低
        if (newNav.compareTo(BigDecimal.valueOf(0.1)) <= 0) {
            newNav = latestNav.multiply(BigDecimal.valueOf(0.98)); // 最多跌2%
        }
        
        return newNav.setScale(4, RoundingMode.HALF_UP);
    }
    
    /**
     * 獲取所有活躍基金數量
     * @return 活躍基金數量
     */
    public int getActiveFundCount() {
        return fundRepository.findByStatus("OPEN").size();
    }
    
    /**
     * 檢查指定日期是否有基金還沒有淨值
     * @param navDate 淨值日期
     * @return 還沒有淨值的基金數量
     */
    public int getPendingNavCount(LocalDate navDate) {
        List<Fund> activeFunds = fundRepository.findByStatus("OPEN");
        int pendingCount = 0;
        
        for (Fund fund : activeFunds) {
            if (!fundNavRepository.existsByFundIdAndNavDate(fund.getFundId(), navDate)) {
                pendingCount++;
            }
        }
        
        return pendingCount;
    }
    
    // 內部枚舉：更新結果類型
    private enum UpdateResult {
        SUCCESS, SKIPPED, ERROR
    }
    
    // 批量更新結果類
    public static class BatchUpdateResult {
        private final int totalCount;
        private final int successCount;
        private final int skippedCount;
        private final int errorCount;
        private final LocalDate navDate;
        
        public BatchUpdateResult(int totalCount, int successCount, 
                               int skippedCount, int errorCount, LocalDate navDate) {
            this.totalCount = totalCount;
            this.successCount = successCount;
            this.skippedCount = skippedCount;
            this.errorCount = errorCount;
            this.navDate = navDate;
        }
        
        // Getters
        public int getTotalCount() { return totalCount; }
        public int getSuccessCount() { return successCount; }
        public int getSkippedCount() { return skippedCount; }
        public int getErrorCount() { return errorCount; }
        public LocalDate getNavDate() { return navDate; }
        
        public boolean isFullSuccess() {
            return errorCount == 0 && successCount > 0;
        }
        
        public double getSuccessRate() {
            return totalCount > 0 ? (double) successCount / totalCount * 100 : 0;
        }
        
        @Override
        public String toString() {
            return String.format("BatchUpdateResult{總計=%d, 成功=%d, 跳過=%d, 錯誤=%d, 日期=%s, 成功率=%.1f%%}",
                               totalCount, successCount, skippedCount, errorCount, navDate, getSuccessRate());
        }
    }
}