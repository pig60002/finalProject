package com.bank.fund.service;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

/**
 * 淨值排程服務
 * 負責自動定期更新基金淨值
 */
@Slf4j
@Service
public class NavSchedulerService {
    
    @Autowired
    private FundNavService fundNavService;
    
    /**
     * 每個工作日下午6點自動更新淨值
     * cron 表達式：秒 分 時 日 月 星期
     * 0 0 18 * * MON-FRI = 週一到週五下午6點
     */
    @Scheduled(cron = "0 0 18 * * MON-FRI", zone = "Asia/Taipei")
    public void scheduledDailyNavUpdate() {
        log.info("開始執行定期淨值更新 - 時間: {}", LocalDateTime.now());
        
        try {
            fundNavService.updateDailyNav();
            log.info("定期淨值更新完成");
        } catch (Exception e) {
            log.error("定期淨值更新失敗", e);
        }
    }
    
    /**
     * 每天凌晨1點清理過舊的淨值記錄（可選）
     * 保留最近一年的淨值記錄
     */
    @Scheduled(cron = "0 0 1 * * *", zone = "Asia/Taipei")
    public void scheduledNavCleanup() {
        log.info("開始執行淨值記錄清理 - 時間: {}", LocalDateTime.now());
        
        try {
            // 清理一年前的淨值記錄
            LocalDate cutoffDate = LocalDate.now().minusYears(1);
            log.info("清理 {} 之前的淨值記錄", cutoffDate);
            
            long deletedCount = fundNavService.cleanupOldNavs(cutoffDate);
            log.info("淨值記錄清理完成，共刪除 {} 筆記錄", deletedCount);
        } catch (Exception e) {
            log.error("淨值記錄清理失敗", e);
        }
    }
    
    /**
     * 手動觸發淨值更新（用於測試或緊急情況）
     */
    public void manualNavUpdate() {
        log.info("手動觸發淨值更新 - 時間: {}", LocalDateTime.now());
        
        try {
            fundNavService.updateDailyNav();
            log.info("手動淨值更新完成");
        } catch (Exception e) {
            log.error("手動淨值更新失敗", e);
            throw new RuntimeException("淨值更新失敗: " + e.getMessage(), e);
        }
    }
    
    /**
     * 手動觸發指定日期的淨值更新
     */
    public void manualNavUpdate(LocalDate targetDate) {
        log.info("手動觸發指定日期淨值更新 - 日期: {}, 時間: {}", targetDate, LocalDateTime.now());
        
        try {
            fundNavService.updateDailyNav(targetDate);
            log.info("手動淨值更新完成 - 日期: {}", targetDate);
        } catch (Exception e) {
            log.error("手動淨值更新失敗 - 日期: {}", targetDate, e);
            throw new RuntimeException("淨值更新失敗: " + e.getMessage(), e);
        }
    }
}