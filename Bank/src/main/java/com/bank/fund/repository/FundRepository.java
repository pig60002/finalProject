package com.bank.fund.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.bank.fund.entity.Fund;

@Repository
public interface FundRepository extends JpaRepository<Fund, Integer> {
    /**
     * 透過 JOIN FETCH 載入基金及其最新淨值記錄
     */
    @Query("SELECT DISTINCT f FROM Fund f " +
           "LEFT JOIN FETCH f.fundNavs fn " +
           "WHERE fn.navDate = (SELECT MAX(fn2.navDate) FROM FundNav fn2 WHERE fn2.fund = f) " +
           "OR f.fundNavs IS EMPTY " +
           "ORDER BY f.fundId")
    List<Fund> findAllWithLatestNav();
    
    /**
     * 取得單一基金及其最新淨值記錄
     */
    @Query("SELECT f FROM Fund f " +
           "LEFT JOIN FETCH f.fundNavs fn " +
           "WHERE f.fundId = :fundId " +
           "AND (fn.navDate = (SELECT MAX(fn2.navDate) FROM FundNav fn2 WHERE fn2.fund = f) " +
           "OR f.fundNavs IS EMPTY)")
    Optional<Fund> findByIdWithLatestNav(@Param("fundId") Integer fundId);
    /**
     * 根據基金代碼查詢基金
     */
    Optional<Fund> findByFundCode(String fundCode);
    
    /**
     * 檢查基金代碼是否已存在
     */
    boolean existsByFundCode(String fundCode);
    
    /**
     * 根據基金名稱查詢基金（模糊查詢）
     */
    List<Fund> findByFundNameContaining(String fundName);
    
    /**
     * 根據基金類型查詢基金
     */
    List<Fund> findByFundType(String fundType);
    
    /**
     * 根據狀態查詢基金
     */
    List<Fund> findByStatus(String status);
    
    /**
     * 根據風險等級查詢基金
     */
    List<Fund> findByRiskLevel(Integer riskLevel);
    
    
    /**
     * 根據幣別查詢基金
     */
    List<Fund> findByCurrency(String currency);
    
    /**
     * 查詢開放中的基金
     */
    @Query("SELECT f FROM Fund f WHERE f.status = 'OPEN'")
    List<Fund> findOpenFunds();
    
    /**
     * 根據公司帳戶查詢基金
     */
    @Query("SELECT f FROM Fund f WHERE f.account.accountId = :accountId")
    List<Fund> findByAccountId(@Param("accountId") Integer accountId);
    
   
    /**
     * 複合查詢：根據多個條件查詢基金
     */
    @Query("SELECT f FROM Fund f WHERE " +
           "(:fundType IS NULL OR f.fundType = :fundType) AND " +
           "(:status IS NULL OR f.status = :status) AND " +
           "(:riskLevel IS NULL OR f.riskLevel = :riskLevel) AND " +
           "(:currency IS NULL OR f.currency = :currency)")
    List<Fund> findByMultipleConditions(@Param("fundType") String fundType,
                                       @Param("status") String status,
                                       @Param("riskLevel") Integer riskLevel,
                                       @Param("currency") String currency);
    
    /**
     * 根據基金ID列表查詢基金
     */
    @Query("SELECT f FROM Fund f WHERE f.fundId IN :fundIds")
    List<Fund> findByFundIdIn(@Param("fundIds") List<Integer> fundIds);
}