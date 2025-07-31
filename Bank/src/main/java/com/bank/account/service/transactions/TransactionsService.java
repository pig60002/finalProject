package com.bank.account.service.transactions;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bank.account.bean.Account;
import com.bank.account.bean.Transactions;
import com.bank.account.dao.TransactionsRepository;
import com.bank.account.service.SerialControlService;
import com.bank.utils.AccountUtils;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class TransactionsService {

	@Autowired
	private TransactionsRepository txRepos;
	
	@Autowired
	private SerialControlService scService;
	
	// 查詢帳戶所有交易
//	public List<Transactions> getTransactionByAccountId(){
//		return null;
//	}
	
	// 新增交易紀錄
	public Transactions saveTransactionsRecord(Account account, String transactionType,
											   String toBankCode, String toAccountId,
											   BigDecimal newBalance,
											   String memo, String TxStatus, Integer operatorId) {
		// 建立交易流水編號
		String transactionId = scService.getSCNB("transactions", AccountUtils.getTodayCode());
		
		Transactions txBean = new Transactions(
				transactionId,
				account != null ? account.getAccountId() : null,
				transactionType,
				toBankCode,
				toAccountId,
				account != null ? account.getCurrency() : null,
				newBalance,
				LocalDateTime.now(),
				memo,
				TxStatus,
				operatorId
				);
		
		
		
		return txRepos.save(txBean);
	}
	
	
	
	
	// 轉帳交易 (內部帳戶)
	
	
}
