package com.bank.fund.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bank.fund.entity.Fund;
import com.bank.fund.entity.FundNav;
import com.bank.fund.repository.FundRepository;
import com.bank.account.bean.Account;
import com.bank.account.dao.AccountRepository;

@Service
public class FundService {
    
    @Autowired
    private FundRepository fundRepository;
    
    @Autowired
    private AccountRepository accountRepository;
    
    @Autowired
    private FundNavService fundNavService;
    
    public List<Fund> getAllFunds() {
        return fundRepository.findAll();
    }
    
    /**
     * 取得帶有最新淨值資訊的所有基金（透過 JOIN FETCH 載入關聯資料）
     */
    @Transactional(readOnly = true)
    public List<Fund> getAllFundsWithLatestNav() {
        return fundRepository.findAllWithLatestNav();
    }
    
    public Optional<Fund> getFundById(Integer id) {
        return fundRepository.findById(id);
    }
    
    /**
     * 取得帶有最新淨值資訊的單一基金
     */
    @Transactional(readOnly = true)
    public Optional<Fund> getFundByIdWithLatestNav(Integer id) {
        return fundRepository.findByIdWithLatestNav(id);
    }
    
    @Transactional
    public Fund createFund(Fund fund) {
        // 檢查基金代碼是否重複
        if (fundRepository.existsByFundCode(fund.getFundCode())) {
            throw new RuntimeException("基金代碼已存在: " + fund.getFundCode());
        }
        
        // 驗證公司帳戶是否存在
        if (fund.getAccount() == null || fund.getAccount().getAccountId() == null) {
            throw new RuntimeException("必須指定公司帳戶");
        }
        
        Account account = accountRepository
            .findById(fund.getAccount().getAccountId())
            .orElseThrow(() -> new RuntimeException("公司帳戶不存在"));
        
        fund.setAccount(account);
        
        // 設置創建時間
        if (fund.getLaunchTime() == null) {
            fund.setLaunchTime(LocalDateTime.now());
        }
        
        // 儲存基金
        Fund savedFund = fundRepository.save(fund);
        
        // 如果有提供淨值資訊（透過建構子或暫時設定），則新增淨值記錄
        // 注意：由於 latestNav 和 navDate 是 @Transient，這裡需要透過其他方式傳遞初始淨值
        
        return savedFund;
    }
    
    /**
     * 創建基金並設定初始淨值
     */
    @Transactional
    public Fund createFundWithInitialNav(Fund fund, BigDecimal initialNav, LocalDate navDate) {
        // 先創建基金
        Fund savedFund = createFund(fund);
        
        // 如果有提供初始淨值，則新增淨值記錄
        if (initialNav != null && initialNav.compareTo(BigDecimal.ZERO) > 0) {
            LocalDate actualNavDate = navDate != null ? navDate : LocalDate.now();
            
            FundNav fundNav = new FundNav();
            fundNav.setFund(savedFund);  // 設定完整的 Fund 物件
            fundNav.setFundId(savedFund.getFundId());  // 明確設定 fundId
            fundNav.setNavDate(actualNavDate);
            fundNav.setNav(initialNav);
            
            // 新增淨值記錄
            fundNavService.create(fundNav);
        }
        
        return savedFund;
    }
    
    @Transactional
    public Fund updateFund(Integer id, Fund fund) {
        Fund existingFund = fundRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("基金不存在"));
        
        // 檢查基金代碼是否與其他基金重複
        if (!existingFund.getFundCode().equals(fund.getFundCode()) && 
            fundRepository.existsByFundCode(fund.getFundCode())) {
            throw new RuntimeException("基金代碼已存在: " + fund.getFundCode());
        }
        
        // 更新基金基本資訊
        existingFund.setFundCode(fund.getFundCode());
        existingFund.setFundName(fund.getFundName());
        existingFund.setFundType(fund.getFundType());
        existingFund.setRiskLevel(fund.getRiskLevel());
        existingFund.setCurrency(fund.getCurrency());
        existingFund.setSize(fund.getSize());
        existingFund.setMinBuy(fund.getMinBuy());
        existingFund.setBuyFee(fund.getBuyFee());
        existingFund.setStatus(fund.getStatus());
        existingFund.setLaunchTime(fund.getLaunchTime());
        
        // 如果公司帳戶有變更，進行驗證和更新
        if (fund.getAccount() != null && fund.getAccount().getAccountId() != null) {
            Account account = accountRepository
                .findById(fund.getAccount().getAccountId())
                .orElseThrow(() -> new RuntimeException("公司帳戶不存在"));
            existingFund.setAccount(account);
        }
        
        // 儲存基金更新
        return fundRepository.save(existingFund);
    }
    
    /**
     * 更新基金並同時更新淨值
     */
    @Transactional
    public Fund updateFundWithNav(Integer id, Fund fund, BigDecimal newNav, LocalDate navDate) {
        Fund updatedFund = updateFund(id, fund);
        
        // 如果提供了新的淨值，新增淨值記錄
        if (newNav != null && newNav.compareTo(BigDecimal.ZERO) > 0) {
            LocalDate actualNavDate = navDate != null ? navDate : LocalDate.now();
            
            FundNav fundNav = new FundNav();
            fundNav.setFund(updatedFund);
            fundNav.setNavDate(actualNavDate);
            fundNav.setNav(newNav);
            
            try {
                fundNavService.create(fundNav);
            } catch (Exception e) {
                System.err.println("更新淨值失敗: " + e.getMessage());
                throw new RuntimeException("更新淨值失敗: " + e.getMessage());
            }
        }
        
        return updatedFund;
    }
    
    @Transactional
    public void deleteFund(Integer id) {
        if (!fundRepository.existsById(id)) {
            throw new RuntimeException("基金不存在");
        }
        
        // 注意：需要考慮是否要同時刪除相關的淨值記錄
        // 這取決於你的業務邏輯，可能需要先刪除相關的交易記錄等
        fundRepository.deleteById(id);
    }
    
    public boolean existsByFundCode(String fundCode) {
        return fundRepository.existsByFundCode(fundCode);
    }
    
    /**
     * 取得基金的最新淨值記錄
     */
    @Transactional(readOnly = true)
    public Optional<FundNav> getLatestNavByFundId(Integer fundId) {
        Fund fund = fundRepository.findById(fundId)
            .orElseThrow(() -> new RuntimeException("基金不存在"));
        return fundNavService.getLatestNavByFund(fund);
    }
    
    /**
     * 取得基金在特定日期的淨值
     */
    @Transactional(readOnly = true)
    public Optional<FundNav> getNavByFundIdAndDate(Integer fundId, LocalDate date) {
        Fund fund = fundRepository.findById(fundId)
            .orElseThrow(() -> new RuntimeException("基金不存在"));
        return fundNavService.getNavByFundAndDate(fund, date);
    }
}