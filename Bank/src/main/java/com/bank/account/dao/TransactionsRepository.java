package com.bank.account.dao;


import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bank.account.bean.Transactions;

public interface TransactionsRepository extends JpaRepository<Transactions, String> {
	
	// 依帳戶查詢交易明細
	@Query("SELECT t FROM Transactions t WHERE t.accountId = :accountId AND ( :start IS NULL OR txTime >= :start ) "
			+ "AND ( :end IS NULL OR txTime < :end ) ORDER BY txTime DESC")
	List<Transactions> findByAccountId(@Param("accountId") String accountId,
									   @Param("start") LocalDateTime start,
									   @Param("end") LocalDateTime end);
	
	// 依帳戶查詢交易明細且status = "交易成功" || status = "轉帳成功"
	@Query("SELECT t FROM Transactions t WHERE t.accountId = :accountId AND ( :start IS NULL OR txTime >= :start ) "
				+ "AND ( :end IS NULL OR txTime < :end )"
				+ "AND (t.status IN :statuses) ORDER BY txTime DESC")
	List<Transactions> findByAccountIdAndStatusIn(@Param("accountId") String accountId,
												  @Param("statuses") List<String> statuses,
											      @Param("start") LocalDateTime start,
											      @Param("end") LocalDateTime end);
}
