package com.bank.account.dao;


import org.springframework.data.jpa.repository.JpaRepository;

import com.bank.account.bean.Transactions;

public interface TransactionsRepository extends JpaRepository<Transactions, String> {
	
	// 帳戶轉入轉出(行員操作) 
//	Transactions update
	
}
