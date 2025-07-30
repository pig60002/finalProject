package com.bank.account.dao;

import org.springframework.data.repository.Repository;

import com.bank.account.bean.Transactions;

public interface TransactionsRepository extends Repository<Transactions, String> {
	
	// 帳戶轉入轉出(行員操作) 
//	Transactions update
	
}
