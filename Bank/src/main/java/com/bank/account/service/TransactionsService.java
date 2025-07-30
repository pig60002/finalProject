package com.bank.account.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bank.account.bean.Transactions;
import com.bank.account.dao.TransactionsRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class TransactionsService {

	@Autowired
	private TransactionsRepository txRepos;
	
	// 查詢帳戶所有交易
	public List<Transactions> getTransactionByAccountId(){
		return null;
	}
	
	// 帳戶轉入轉出(行員操作)
	
	
	
}
