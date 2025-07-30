package com.bank.account.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bank.account.bean.Account;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


public interface AccountRepository extends JpaRepository<Account, String> {
	
	List<Account> findByMId(Integer mId);
	
	// 修改帳戶狀態 
	@Modifying
	@Query("UPDATE Account a SET a.status=:status, a.memo=:memo, a.operatorId=:operatorId,"
			+ "a.statusUpdatedTime=:statusUpdateTime WHERE a.accountId=:accountId")
	int updateAccountStatus(@Param("status") String status,
						  	@Param("memo")   String memo,
						  	@Param("operatorId") Integer operatorId,
						  	@Param("statusUpdateTime") LocalDateTime statusUpdateTime,
						  	@Param("accountId") String accountId);
	
	// 修改帳戶餘額
	@Query("UPDATE Account a SET a.balance=:balance WHERE a.accountId=:accountId")
	int updateAccountBalance(@Param("balance")   BigDecimal balance,
							 @Param("accountId") String accountId);
	
	// 依帳號查詢帳戶
	Account findByAccountId(String accountId);
}
