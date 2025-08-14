package com.bank.account.service.transactions;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

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
	public List<Transactions> getTransactionByAccountId(String accountId, String startDate, String endDate){
		
		LocalDateTime startOfDay = AccountUtils.getStartOfDay(startDate);
		LocalDateTime endExclusiveDate = AccountUtils.getEndExclusiveDate(endDate);
		
		return txRepos.findByAccountId(accountId, startOfDay, endExclusiveDate);
	}
	
	// 查詢帳戶所有"成功"交易
	public List<Transactions> getTxSuccessRecords(String accountId, String startDate, String endDate){
		List<String> statusList = List.of("交易成功","轉帳成功");
		
		LocalDateTime startOfDay = AccountUtils.getStartOfDay(startDate);
		LocalDateTime endExclusiveDate = AccountUtils.getEndExclusiveDate(endDate);
		
		return txRepos.findByAccountIdAndStatusIn(accountId, statusList, startOfDay, endExclusiveDate);
	}
	
	// 新增交易紀錄
	public Transactions saveTransactionsRecord(Account account, String transactionType,
											   String toBankCode, String toAccountId,
											   BigDecimal amount, BigDecimal balanceAfter,
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
				amount,
				LocalDateTime.now(),
				memo,
				TxStatus,
				operatorId,
				balanceAfter
				);
		
		
		
		return txRepos.save(txBean);
	}
	
	// 找最近有轉出的帳號
	public List<String> findOutgoingSuccess(String accountId){
		return txRepos.findOutgoingSuccess(accountId);
	}
}
