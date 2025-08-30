package com.bank.fund.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bank.fund.dto.FundDto;
import com.bank.fund.entity.Fund;
import com.bank.fund.entity.FundNav;
import com.bank.fund.repository.FundNavRepository;
import com.bank.fund.repository.FundRepository;

@Service
public class FundNavService {
    
    @Autowired
    private FundNavRepository fundNavRepository;
    
    @Autowired
    private FundRepository fundRepository;
    
    @Transactional(readOnly = true)
    public List<FundNav> getByFundId(Integer fundId) {
        return fundNavRepository.findByFundFundIdOrderByNavDateDesc(fundId);
    }

    @Transactional
    public long cleanupOldNavs(LocalDate cutoffDate) {
        long count = fundNavRepository.countByNavDateBefore(cutoffDate);
        fundNavRepository.deleteByNavDateBefore(cutoffDate);
        
        System.out.println("已清理 " + count + " 筆 " + cutoffDate + " 之前的淨值記錄");
        return count;
    }
    
    @Transactional
    public long cleanupOldNavsForFund(Fund fund, LocalDate cutoffDate) {
        long count = fundNavRepository.countByFundAndNavDateBefore(fund, cutoffDate);
        fundNavRepository.deleteByFundAndNavDateBefore(fund, cutoffDate);
        
        System.out.println("基金 " + fund.getFundCode() + " 已清理 " + count + " 筆舊記錄");
        return count;
    }
    
    @Transactional(readOnly = true)
    public Optional<FundNav> getById(Integer id) {
        return fundNavRepository.findById(id);
    }
    
    @Transactional
    public FundNav create(FundNav fundNav) {
        Optional<FundNav> existingNav = fundNavRepository
            .findByFundAndNavDate(fundNav.getFund(), fundNav.getNavDate());
        
        if (existingNav.isPresent()) {
            FundNav existing = existingNav.get();
            existing.setNav(fundNav.getNav());
            return fundNavRepository.save(existing);
        } else {
            return fundNavRepository.save(fundNav);
        }
    }
    
    @Transactional
    public FundNav update(Integer id, FundNav fundNav) {
        FundNav existing = fundNavRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("淨值記錄不存在"));
        
        existing.setNavDate(fundNav.getNavDate());
        existing.setNav(fundNav.getNav());
        
        return fundNavRepository.save(existing);
    }
    
    public void delete(Integer id) {
        if (!fundNavRepository.existsById(id)) {
            throw new RuntimeException("淨值記錄不存在");
        }
        fundNavRepository.deleteById(id);
    }
    
    @Transactional(readOnly = true)
    public Optional<FundNav> getLatestNavByFund(Fund fund) {
        return fundNavRepository.findTopByFundOrderByNavDateDesc(fund);
    }
    
    @Transactional(readOnly = true)
    public Optional<FundNav> getNavByFundAndDate(Fund fund, LocalDate date) {
        return fundNavRepository.findByFundAndNavDate(fund, date);
    }
    
    @Transactional
    public List<FundNav> createOrUpdateBatch(List<FundNav> fundNavs) {
        return fundNavs.stream()
            .map(this::create)
            .toList();
    }
    
    @Transactional
    public void updateDailyNav() {
        updateDailyNav(LocalDate.now());
    }
    
    @Transactional
    public void updateDailyNav(LocalDate targetDate) {
        if (isWeekend(targetDate)) {
            System.out.println("週末不更新淨值: " + targetDate);
            return;
        }
        
        // 直接查詢所有基金
        List<Fund> allFunds = fundRepository.findAll();
        int updatedCount = 0;
        int skippedCount = 0;
        
        for (Fund fund : allFunds) {
            try {
                // 檢查該日期是否已有淨值
                Optional<FundNav> existingNav = fundNavRepository.findByFundAndNavDate(fund, targetDate);
                
                if (existingNav.isPresent()) {
                    System.out.println("基金 " + fund.getFundCode() + " 在 " + targetDate + " 已有淨值記錄，跳過更新");
                    skippedCount++;
                    continue;
                }
                
                // 獲取基金最新淨值（透過查詢 FundNav 表）
                Optional<FundNav> latestNavOpt = fundNavRepository.findTopByFundOrderByNavDateDesc(fund);
                BigDecimal currentNav = latestNavOpt.map(FundNav::getNav).orElse(BigDecimal.valueOf(10.0));
                
                // 生成新的模擬淨值
                BigDecimal newNav = generateSimulatedNav(currentNav, fund.getRiskLevel());
                
                // 確保淨值不會小於0
                if (newNav.compareTo(BigDecimal.ZERO) <= 0) {
                    newNav = currentNav.multiply(new BigDecimal("0.9"));
                }
                
                // 建立淨值記錄
                FundNav fundNav = new FundNav();
                fundNav.setFund(fund);
                fundNav.setNavDate(targetDate);
                fundNav.setNav(newNav);
                
                fundNavRepository.save(fundNav);
                updatedCount++;
                
            } catch (Exception e) {
                System.err.println("更新基金淨值失敗 - 基金ID: " + fund.getFundId() + ", 錯誤: " + e.getMessage());
            }
        }
        
        System.out.println("淨值更新完成 - 日期: " + targetDate + ", 更新: " + updatedCount + " 筆, 跳過: " + skippedCount + " 筆");
    }
    
    private BigDecimal generateSimulatedNav(BigDecimal currentNav, Integer riskLevel) {
        if (currentNav == null || riskLevel == null) {
            return BigDecimal.TEN; // 預設淨值
        }
        
        // 根據風險等級設定波動範圍
        double volatilityFactor = switch (riskLevel) {
            case 1 -> 0.005; // RR1: 0.5% 波動
            case 2 -> 0.01;  // RR2: 1% 波動
            case 3 -> 0.02;  // RR3: 2% 波動
            case 4 -> 0.03;  // RR4: 3% 波動
            case 5 -> 0.05;  // RR5: 5% 波動
            default -> 0.02;
        };
        
        // 生成 -volatilityFactor 到 +volatilityFactor 之間的隨機變動
        double randomChange = (ThreadLocalRandom.current().nextDouble() - 0.5) * 2 * volatilityFactor;
        
        // 加入一點正向偏向（長期上漲趨勢）
        double trendBias = 0.0001; // 0.01% 正向偏向
        double totalChange = randomChange + trendBias;
        
        // 計算新淨值
        BigDecimal changeAmount = currentNav.multiply(BigDecimal.valueOf(totalChange));
        BigDecimal newNav = currentNav.add(changeAmount);
        
        // 四捨五入到小數點後4位
        return newNav.setScale(4, BigDecimal.ROUND_HALF_UP);
    }
    
    private boolean isWeekend(LocalDate date) {
        int dayOfWeek = date.getDayOfWeek().getValue();
        return dayOfWeek == 6 || dayOfWeek == 7; // 週六或週日
    }
    
    @Transactional
    public FundNav updateSingleFundNav(Integer fundId, BigDecimal nav, LocalDate navDate) {
        Fund fund = fundRepository.findById(fundId)
            .orElseThrow(() -> new RuntimeException("基金不存在"));
        
        FundNav fundNav = new FundNav();
        fundNav.setFund(fund);
        fundNav.setNavDate(navDate);
        fundNav.setNav(nav);
        
        // 使用 create 方法來處理重複日期的情況
        return create(fundNav);
    }
}