package com.bank.account.service.transactions;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bank.account.bean.Account;
import com.bank.account.bean.Transactions;
import com.bank.account.service.AccountServcie;
import com.bank.utils.BankConstants;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class InternalTransferService {
	
	@Autowired
	private TransactionsService txService;
	
	@Autowired
	private AccountServcie accountServcie;

	// 銀行內部轉帳
	public Transactions internalTransferAction(Transactions txRequest) {
		
		// 接收請求
		String accountId = txRequest.getAccountId();
		String transactionType = txRequest.getTransactionType();
		String toBankCode = BankConstants.SELF_BANK_CODE_STRING;
		String toAccountId = txRequest.getToAccountId();
		BigDecimal amount = txRequest.getAmount();
		String memo = txRequest.getMemo();
		Integer operatorId = txRequest.getOperatorId();
		
		String txStatus = "交易失敗"; // 預設交易失敗
		
		Account account = accountServcie.getByAccountId(accountId);
		BigDecimal accountBalance = account.getBalance();
		BigDecimal accountNewBalance = null;
		
		Account toAccount = accountServcie.getByAccountId(toAccountId);
		BigDecimal toAccountBalance = toAccount.getBalance();
		BigDecimal toAccountNewBalance = null;
		
		// 查詢兩個帳戶 (兩者皆存在且不是同一個帳戶&幣別相等)
		if( account == null || toAccount == null || !account.getCurrency().equals(toAccount.getCurrency()) || accountId.equals(toAccountId)) {
			memo = "帳號錯誤或幣別不相等";
			return txService.saveTransactionsRecord(account, transactionType, toBankCode, toAccountId, accountBalance, memo, txStatus,
					operatorId);
		}
		
		// 檢查交易金額
		if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
			memo = "交易金額輸入錯誤";
			return txService.saveTransactionsRecord(account, transactionType, toBankCode, toAccountId, accountBalance, memo, txStatus,
					operatorId);
		}
		
		// 檢查交易類型
		if ( !"內部轉帳".equals(transactionType) ) {
			memo = "交易類型錯誤";
			return txService.saveTransactionsRecord(account, transactionType, toBankCode, toAccountId, accountBalance, memo, txStatus,
					operatorId);
		}
		
		// 帳戶1 扣款
		if( accountBalance.compareTo(amount) >= 0 ) {
			// .subtract() 減法
			accountNewBalance = accountBalance.subtract(amount);
		} else {
			memo = "餘額不足";
			return txService.saveTransactionsRecord(account, transactionType, toBankCode, toAccountId, accountBalance, memo, txStatus,
					operatorId);
		}
		
		// 帳戶2 收款
		toAccountNewBalance = toAccountBalance.add(amount);
		
		
		// 更新兩者帳戶餘額
		int updateRS1 = accountServcie.updateAccountBalance(accountId, accountNewBalance);
		int updateRS2 = accountServcie.updateAccountBalance(toAccountId, toAccountNewBalance);
		
		if(updateRS1 != 1 || updateRS2 !=1) {
			memo = "更新帳戶餘額失敗";
			return txService.saveTransactionsRecord(account, transactionType, toBankCode, toAccountId, accountBalance, memo, txStatus,
					operatorId);
		}
		txStatus = "轉帳成功";
		
		// 各建立一筆交易紀錄
		txService.saveTransactionsRecord(toAccount, transactionType, toBankCode, accountId, toAccountNewBalance, memo, txStatus, operatorId);
		
		return txService.saveTransactionsRecord(account, transactionType, toBankCode, toAccountId, accountNewBalance, memo, txStatus, operatorId);
		
	}
	
}
