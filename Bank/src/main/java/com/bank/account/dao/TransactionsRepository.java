package com.bank.account.dao;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bank.account.bean.Transactions;

public interface TransactionsRepository extends JpaRepository<Transactions, String> {
	
	// 依帳戶查詢交易明細
	List<Transactions> findByAccountId(String accountId);
	
	// 依帳戶查詢交易明細且status = "交易成功" || status = "轉帳成功"
	List<Transactions> findByAccountIdAndStatusIn( String accountId, List<String> statusList );
}
