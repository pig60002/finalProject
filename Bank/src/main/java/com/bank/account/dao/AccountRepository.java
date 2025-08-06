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
	@Modifying
	@Query("UPDATE Account a SET a.balance=:balance WHERE a.accountId=:accountId")
	int updateAccountBalance(@Param("balance")   BigDecimal balance,
							 @Param("accountId") String accountId);
	
	// 依帳號查詢帳戶
	Account findByAccountId(String accountId);
	
	Account findByMIdAndAccountNameAndCurrency(Integer mId, String accountName,String Currency);

	// 多欄位查詢帳戶
	@Query("SELECT a FROM Account a WHERE (:mId IS NULL OR a.mId =:mId) AND "
			+ "(:mIdentity IS NULL OR a.member.mIdentity LIKE %:mIdentity%) AND "
			+ "(:mPhone IS NULL OR a.member.mPhone LIKE %:mPhone%) AND "
			+ "(:mName IS NULL OR a.member.mName LIKE %:mName%) AND "
			+ "(:accountId IS NULL OR a.accountId LIKE %:accountId%)")
	List<Account> searchAccounts(@Param("mId") Integer mId,
								 @Param("mIdentity") String mIdentity,
								 @Param("mPhone") String mPhone,
								 @Param("mName") String mName,
								 @Param("accountId") String accountId);



}

