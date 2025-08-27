package com.bank.fund.repository;

import com.bank.fund.dto.FundDto;
import com.bank.fund.entity.Fund;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FundRepository extends JpaRepository<Fund, Integer> {

    //查詢所有基金並包含最新淨值
    @Query("SELECT new com.bank.fund.dto.FundDto(" +
           "f.fundId, f.fundCode, f.fundName, fn.nav, fn.navDate, " +
           "f.fundType, f.riskLevel, f.buyFee, f.status) " +
           "FROM Fund f LEFT JOIN FundNav fn ON f.fundId = fn.fund.fundId " +
           "WHERE fn.navDate = (SELECT MAX(fn2.navDate) FROM FundNav fn2 WHERE fn2.fund.fundId = f.fundId) " +
           "OR fn.navDate IS NULL")
    List<FundDto> findAllFundsWithLatestNav();

    //根據基金ID查詢單一基金並包含最新淨值
    @Query("SELECT new com.bank.fund.dto.FundDto(" +
           "f.fundId, f.fundCode, f.fundName, fn.nav, fn.navDate, " +
           "f.fundType, f.riskLevel, f.buyFee, f.status) " +
           "FROM Fund f LEFT JOIN FundNav fn ON f.fundId = fn.fund.fundId " +
           "WHERE f.fundId = :fundId " +
           "AND (fn.navDate = (SELECT MAX(fn2.navDate) FROM FundNav fn2 WHERE fn2.fund.fundId = f.fundId) " +
           "OR fn.navDate IS NULL)")
    Optional<FundDto> findFundWithLatestNavById(@Param("fundId") Integer fundId);

    //根據基金ID列表查詢多個基金並包含最新淨值
    @Query("SELECT new com.bank.fund.dto.FundDto(" +
           "f.fundId, f.fundCode, f.fundName, fn.nav, fn.navDate, " +
           "f.fundType, f.riskLevel, f.buyFee, f.status) " +
           "FROM Fund f LEFT JOIN FundNav fn ON f.fundId = fn.fund.fundId " +
           "WHERE f.fundId IN :fundIds " +
           "AND (fn.navDate = (SELECT MAX(fn2.navDate) FROM FundNav fn2 WHERE fn2.fund.fundId = f.fundId) " +
           "OR fn.navDate IS NULL)")
    List<FundDto> findFundsWithLatestNavByIds(@Param("fundIds") List<Integer> fundIds);

    //根據基金類型查詢基金並包含最新淨值
    @Query("SELECT new com.bank.fund.dto.FundDto(" +
           "f.fundId, f.fundCode, f.fundName, fn.nav, fn.navDate, " +
           "f.fundType, f.riskLevel, f.buyFee, f.status) " +
           "FROM Fund f LEFT JOIN FundNav fn ON f.fundId = fn.fund.fundId " +
           "WHERE f.fundType = :fundType " +
           "AND (fn.navDate = (SELECT MAX(fn2.navDate) FROM FundNav fn2 WHERE fn2.fund.fundId = f.fundId) " +
           "OR fn.navDate IS NULL)")
    List<FundDto> findFundsByTypeWithLatestNav(@Param("fundType") String fundType);

    //根據風險等級查詢基金並包含最新淨值
    @Query("SELECT new com.bank.fund.dto.FundDto(" +
           "f.fundId, f.fundCode, f.fundName, fn.nav, fn.navDate, " +
           "f.fundType, f.riskLevel, f.buyFee, f.status) " +
           "FROM Fund f LEFT JOIN FundNav fn ON f.fundId = fn.fund.fundId " +
           "WHERE f.riskLevel = :riskLevel " +
           "AND (fn.navDate = (SELECT MAX(fn2.navDate) FROM FundNav fn2 WHERE fn2.fund.fundId = f.fundId) " +
           "OR fn.navDate IS NULL)")
    List<FundDto> findFundsByRiskLevelWithLatestNav(@Param("riskLevel") Integer riskLevel);

    //根據狀態查詢基金並包含最新淨值
    @Query("SELECT new com.bank.fund.dto.FundDto(" +
           "f.fundId, f.fundCode, f.fundName, fn.nav, fn.navDate, " +
           "f.fundType, f.riskLevel, f.buyFee, f.status) " +
           "FROM Fund f LEFT JOIN FundNav fn ON f.fundId = fn.fund.fundId " +
           "WHERE f.status = :status " +
           "AND (fn.navDate = (SELECT MAX(fn2.navDate) FROM FundNav fn2 WHERE fn2.fund.fundId = f.fundId) " +
           "OR fn.navDate IS NULL)")
    List<FundDto> findFundsByStatusWithLatestNav(@Param("status") String status);

    //使用原生SQL查詢所有基金並包含最新淨值（效能更好）
    @Query(value = """
        SELECT f.fund_id, f.fund_code, f.fund_name, latest_nav.nav, latest_nav.nav_date,
               f.fund_type, f.risk_level, f.buy_fee, f.status
        FROM fund f
        LEFT JOIN (
            SELECT fund_id, nav, nav_date,
                   ROW_NUMBER() OVER (PARTITION BY fund_id ORDER BY nav_date DESC) as rn
            FROM fund_nav
        ) latest_nav ON f.fund_id = latest_nav.fund_id AND latest_nav.rn = 1
        """, nativeQuery = true)
    List<Object[]> findAllFundsWithLatestNavNative();

    //根據基金代碼模糊查詢
    @Query("SELECT new com.bank.fund.dto.FundDto(" +
           "f.fundId, f.fundCode, f.fundName, fn.nav, fn.navDate, " +
           "f.fundType, f.riskLevel, f.buyFee, f.status) " +
           "FROM Fund f LEFT JOIN FundNav fn ON f.fundId = fn.fund.fundId " +
           "WHERE f.fundCode LIKE %:fundCode% " +
           "AND (fn.navDate = (SELECT MAX(fn2.navDate) FROM FundNav fn2 WHERE fn2.fund.fundId = f.fundId) " +
           "OR fn.navDate IS NULL)")
    List<FundDto> findFundsByCodeContainingWithLatestNav(@Param("fundCode") String fundCode);

    //根據基金名稱模糊查詢
    @Query("SELECT new com.bank.fund.dto.FundDto(" +
           "f.fundId, f.fundCode, f.fundName, fn.nav, fn.navDate, " +
           "f.fundType, f.riskLevel, f.buyFee, f.status) " +
           "FROM Fund f LEFT JOIN FundNav fn ON f.fundId = fn.fund.fundId " +
           "WHERE f.fundName LIKE %:fundName% " +
           "AND (fn.navDate = (SELECT MAX(fn2.navDate) FROM FundNav fn2 WHERE fn2.fund.fundId = f.fundId) " +
           "OR fn.navDate IS NULL)")
    List<FundDto> findFundsByNameContainingWithLatestNav(@Param("fundName") String fundName);

    //複合查詢：根據多個條件查詢基金
    @Query("SELECT new com.bank.fund.dto.FundDto(" +
           "f.fundId, f.fundCode, f.fundName, fn.nav, fn.navDate, " +
           "f.fundType, f.riskLevel, f.buyFee, f.status) " +
           "FROM Fund f LEFT JOIN FundNav fn ON f.fundId = fn.fund.fundId " +
           "WHERE (:fundType IS NULL OR f.fundType = :fundType) " +
           "AND (:riskLevel IS NULL OR f.riskLevel = :riskLevel) " +
           "AND (:status IS NULL OR f.status = :status) " +
           "AND (fn.navDate = (SELECT MAX(fn2.navDate) FROM FundNav fn2 WHERE fn2.fund.fundId = f.fundId) " +
           "OR fn.navDate IS NULL)")
    List<FundDto> findFundsByConditionsWithLatestNav(
        @Param("fundType") String fundType,
        @Param("riskLevel") Integer riskLevel,
        @Param("status") String status);
}