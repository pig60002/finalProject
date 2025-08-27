package com.bank.fund.repository;

import com.bank.fund.entity.FundHoldings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FundHoldingsRepository extends JpaRepository<FundHoldings, Integer> {
	Optional<FundHoldings> findByFundAccountFundAccIdAndFundFundId(Integer fundAccId, Integer fundId);

    /**
     * 根據基金帳戶ID查詢所有持有基金並包含最新淨值 - 使用原生SQL
     */
    @Query(value = """
        SELECT fh.holding_id, fh.fund_acc_id, f.fund_id, fh.units, fh.cost,
               f.fund_code, f.fund_name, f.fund_type, f.risk_level, f.buy_fee, f.status,
               latest_nav.nav, latest_nav.nav_date
        FROM fund_holdings fh
        JOIN fund f ON fh.fund_id = f.fund_id
        LEFT JOIN (
            SELECT fund_id, nav, nav_date,
                   ROW_NUMBER() OVER (PARTITION BY fund_id ORDER BY nav_date DESC) as rn
            FROM fund_nav
        ) latest_nav ON f.fund_id = latest_nav.fund_id AND latest_nav.rn = 1
        WHERE fh.fund_acc_id = :fundAccId
        ORDER BY fh.holding_id
        """, nativeQuery = true)
    List<Object[]> findHoldingsByFundAccId(@Param("fundAccId") Integer fundAccId);

    /**
     * 根據基金帳戶ID和基金ID查詢特定持有基金
     */
    @Query(value = """
        SELECT fh.holding_id, fh.fund_acc_id, f.fund_id, fh.units, fh.cost,
               f.fund_code, f.fund_name, f.fund_type, f.risk_level, f.buy_fee, f.status,
               latest_nav.nav, latest_nav.nav_date
        FROM fund_holdings fh
        JOIN fund f ON fh.fund_id = f.fund_id
        LEFT JOIN (
            SELECT fund_id, nav, nav_date,
                   ROW_NUMBER() OVER (PARTITION BY fund_id ORDER BY nav_date DESC) as rn
            FROM fund_nav
        ) latest_nav ON f.fund_id = latest_nav.fund_id AND latest_nav.rn = 1
        WHERE fh.fund_acc_id = :fundAccId AND f.fund_id = :fundId
        """, nativeQuery = true)
    List<Object[]> findHoldingByFundAccIdAndFundId(@Param("fundAccId") Integer fundAccId, 
                                                    @Param("fundId") Integer fundId);

    /**
     * 複合查詢：根據多個條件查詢持有基金
     */
    @Query(value = """
        SELECT fh.holding_id, fh.fund_acc_id, f.fund_id, fh.units, fh.cost,
               f.fund_code, f.fund_name, f.fund_type, f.risk_level, f.buy_fee, f.status,
               latest_nav.nav, latest_nav.nav_date
        FROM fund_holdings fh
        JOIN fund f ON fh.fund_id = f.fund_id
        LEFT JOIN (
            SELECT fund_id, nav, nav_date,
                   ROW_NUMBER() OVER (PARTITION BY fund_id ORDER BY nav_date DESC) as rn
            FROM fund_nav
        ) latest_nav ON f.fund_id = latest_nav.fund_id AND latest_nav.rn = 1
        WHERE fh.fund_acc_id = :fundAccId
        AND (:fundType IS NULL OR f.fund_type = :fundType)
        AND (:riskLevel IS NULL OR f.risk_level = :riskLevel)
        AND (:status IS NULL OR f.status = :status)
        ORDER BY fh.holding_id
        """, nativeQuery = true)
    List<Object[]> findHoldingsByConditions(
        @Param("fundAccId") Integer fundAccId,
        @Param("fundType") String fundType,
        @Param("riskLevel") Integer riskLevel,
        @Param("status") String status);

    /**
     * 查詢持有基金的統計資訊
     */
    @Query(value = """
        SELECT COUNT(*), SUM(fh.units), SUM(fh.cost)
        FROM fund_holdings fh 
        WHERE fh.fund_acc_id = :fundAccId
        """, nativeQuery = true)
    Object[] getHoldingsSummary(@Param("fundAccId") Integer fundAccId);

    /**
     * 使用原生SQL查詢（效能更好）
     */
    @Query(value = """
        SELECT fh.holding_id, fh.fund_acc_id, fh.fund_id, fh.units, fh.cost,
               f.fund_code, f.fund_name, f.fund_type, f.risk_level, f.buy_fee, f.status,
               latest_nav.nav, latest_nav.nav_date
        FROM fund_holdings fh
        JOIN fund f ON fh.fund_id = f.fund_id
        LEFT JOIN (
            SELECT fund_id, nav, nav_date,
                   ROW_NUMBER() OVER (PARTITION BY fund_id ORDER BY nav_date DESC) as rn
            FROM fund_nav
        ) latest_nav ON f.fund_id = latest_nav.fund_id AND latest_nav.rn = 1
        WHERE fh.fund_acc_id = :fundAccId
        ORDER BY fh.holding_id
        """, nativeQuery = true)
    List<Object[]> findHoldingsByFundAccIdNative(@Param("fundAccId") Integer fundAccId);

    /**
     * 如果 FundHoldings 實體中沒有 fund 關係屬性，請使用以下原生SQL版本
     */
    @Query(value = """
        SELECT fh.holding_id, fh.fund_acc_id, f.fund_id, fh.units, fh.cost,
               f.fund_code, f.fund_name, f.fund_type, f.risk_level, f.buy_fee, f.status,
               latest_nav.nav, latest_nav.nav_date
        FROM fund_holdings fh
        JOIN fund f ON fh.fund_id = f.fund_id
        LEFT JOIN (
            SELECT fund_id, nav, nav_date,
                   ROW_NUMBER() OVER (PARTITION BY fund_id ORDER BY nav_date DESC) as rn
            FROM fund_nav
        ) latest_nav ON f.fund_id = latest_nav.fund_id AND latest_nav.rn = 1
        WHERE fh.fund_acc_id = :fundAccId
        AND (:fundType IS NULL OR f.fund_type = :fundType)
        AND (:riskLevel IS NULL OR f.risk_level = :riskLevel)
        AND (:status IS NULL OR f.status = :status)
        ORDER BY fh.holding_id
        """, nativeQuery = true)
    List<Object[]> findHoldingsByConditionsNative(
        @Param("fundAccId") Integer fundAccId,
        @Param("fundType") String fundType,
        @Param("riskLevel") Integer riskLevel,
        @Param("status") String status);
}