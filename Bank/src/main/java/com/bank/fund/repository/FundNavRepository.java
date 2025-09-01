package com.bank.fund.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.bank.fund.entity.Fund;
import com.bank.fund.entity.FundNav;

@Repository
public interface FundNavRepository extends JpaRepository<FundNav, Integer> {
    
    /**
     * 根據基金ID查詢淨值記錄，按日期降序排列
     */
    List<FundNav> findByFundFundIdOrderByNavDateDesc(Integer fundId);
    
    /**
     * 根據基金ID查詢淨值記錄（原有方法，保持向後兼容）
     */
    List<FundNav> findByFundFundId(Integer fundId);
    
    /**
     * 根據基金和日期查詢特定的淨值記錄
     */
    Optional<FundNav> findByFundAndNavDate(Fund fund, LocalDate navDate);
    
    /**
     * 查詢指定基金的最新淨值記錄
     */
    Optional<FundNav> findTopByFundOrderByNavDateDesc(Fund fund);
    
    /**
     * 根據基金ID查詢最新淨值記錄（原有方法，保持向後兼容）
     */
    Optional<FundNav> findTopByFundFundIdOrderByNavDateDesc(Integer fundId);
    
    /**
     * 查詢指定基金在某個日期之前的最新淨值
     */
    Optional<FundNav> findTopByFundAndNavDateLessThanEqualOrderByNavDateDesc(Fund fund, LocalDate date);
    
    /**
     * 檢查指定基金和日期是否已存在淨值記錄
     */
    boolean existsByFundAndNavDate(Fund fund, LocalDate navDate);
    
    /**
     * 根據基金查詢所有淨值記錄，按日期降序排列
     */
    List<FundNav> findByFundOrderByNavDateDesc(Fund fund);
    
    /**
     * 根據日期範圍查詢淨值記錄
     */
    List<FundNav> findByFundAndNavDateBetweenOrderByNavDateDesc(Fund fund, LocalDate startDate, LocalDate endDate);
    
    /**
     * 根據基金ID和日期範圍查詢淨值記錄
     */
    List<FundNav> findByFundFundIdAndNavDateBetweenOrderByNavDateDesc(Integer fundId, LocalDate startDate, LocalDate endDate);
    
    /**
     * 刪除指定日期之前的舊淨值記錄
     */
    void deleteByNavDateBefore(LocalDate cutoffDate);
    
    /**
     * 刪除指定基金在指定日期之前的舊淨值記錄
     */
    void deleteByFundAndNavDateBefore(Fund fund, LocalDate cutoffDate);
    
    /**
     * 計算指定日期之前的記錄數量
     */
    long countByNavDateBefore(LocalDate cutoffDate);
    
    /**
     * 計算指定基金在指定日期之前的記錄數量
     */
    long countByFundAndNavDateBefore(Fund fund, LocalDate cutoffDate);
    
    // ===== Service中需要使用的新增方法 =====
    
    /**
     * 檢查指定基金ID和日期是否已存在淨值記錄（兼容Service中的用法）
     */
    boolean existsByFundIdAndNavDate(Integer fundId, LocalDate navDate);
    
    /**
     * 根據基金ID和日期查詢特定的淨值記錄
     */
    Optional<FundNav> findByFundIdAndNavDate(Integer fundId, LocalDate navDate);
    
    /**
     * 獲取指定基金在指定日期之後的淨值記錄，按日期降序排列
     */
    List<FundNav> findByFundIdAndNavDateAfterOrderByNavDateDesc(Integer fundId, LocalDate navDate);
    
    /**
     * 根據日期查詢所有淨值記錄
     */
    List<FundNav> findByNavDate(LocalDate navDate);
    
    /**
     * 刪除指定基金和日期的淨值記錄
     */
    void deleteByFundIdAndNavDate(Integer fundId, LocalDate navDate);
    
    /**
     * 獲取基金淨值歷史統計（最小值、最大值、平均值、記錄數）
     */
    @Query("SELECT MIN(n.nav), MAX(n.nav), AVG(n.nav), COUNT(n) " +
           "FROM FundNav n WHERE n.fundId = :fundId " +
           "AND n.navDate >= :startDate")
    Object[] getNavStatistics(@Param("fundId") Integer fundId, 
                             @Param("startDate") LocalDate startDate);
    
    /**
     * 獲取基金最近N天的淨值記錄
     */
    @Query(value = "SELECT * FROM fund_nav WHERE fund_id = :fundId " +
                   "ORDER BY nav_date DESC LIMIT :limit", nativeQuery = true)
    List<FundNav> findRecentNavs(@Param("fundId") Integer fundId, 
                                @Param("limit") int limit);
    
    /**
     * 根據基金ID查詢淨值記錄，按日期降序排列（限制數量）
     */
    @Query("SELECT n FROM FundNav n WHERE n.fundId = :fundId " +
           "ORDER BY n.navDate DESC")
    List<FundNav> findByFundIdOrderByNavDateDesc(@Param("fundId") Integer fundId);
}