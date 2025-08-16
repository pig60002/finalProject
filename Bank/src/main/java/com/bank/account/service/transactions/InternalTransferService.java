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
		String requestTxType = txRequest.getTransactionType();
		String toBankCode = BankConstants.SELF_BANK_CODE_STRING;
		String toAccountId = txRequest.getToAccountId();
		BigDecimal amount = txRequest.getAmount();
		String memo = txRequest.getMemo();
		Integer operatorId = txRequest.getOperatorId();
		
		String txStatus = "交易失敗"; // 預設交易失敗
		String transactionType = "內部轉帳".equals(requestTxType) ? "轉出" : requestTxType ;
		
		Account account = accountServcie.getByAccountId(accountId);
		BigDecimal accountBalance = account.getBalance();
		BigDecimal accountNewBalance = null;
		
		Account toAccount = accountServcie.getByAccountId(toAccountId);
		BigDecimal toAccountBalance = toAccount.getBalance();
		BigDecimal toAccountNewBalance = null;
		
		// 檢查交易類型
		if ( !"內部轉帳".equals(requestTxType) && !"信用卡扣款".equals(requestTxType) && !"基金扣款".equals(requestTxType)) {
			memo = "交易類型錯誤";
			return txService.saveTransactionsRecord(account, transactionType, toBankCode, toAccountId, amount, accountBalance, memo, txStatus,
					operatorId);
		}
		
		// 查詢兩個帳戶 (兩者皆存在且不是同一個帳戶&幣別相等)
		if( account == null || toAccount == null) {
			memo = "帳號不存在";
			return txService.saveTransactionsRecord(account, transactionType, toBankCode, toAccountId, amount, accountBalance, memo, txStatus,
					operatorId);
		}
		
		if(!account.getCurrency().equals(toAccount.getCurrency()) || accountId.equals(toAccountId)) {
			memo = "幣別不一致或轉帳帳戶相同";
			return txService.saveTransactionsRecord(account, transactionType, toBankCode, toAccountId, amount, accountBalance, memo, txStatus,
					operatorId);
		}
		
		// 檢查帳戶狀態
		if("限制".equals(account.getStatus()) || "凍結".equals(account.getStatus())) {
			memo = "此帳戶被凍結或限制，目前無法轉帳";
			return txService.saveTransactionsRecord(account, transactionType, toBankCode, toAccountId, amount, accountBalance, memo, txStatus,
					operatorId);
		}
		
		if("凍結".equals(toAccount.getStatus())) {
			memo = "轉入帳戶已凍結，轉帳失敗";
			return txService.saveTransactionsRecord(account, transactionType, toBankCode, toAccountId, amount, accountBalance, memo, txStatus,
					operatorId);
		}
		
		// 檢查交易金額
		if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
			memo = "交易金額輸入錯誤";
			return txService.saveTransactionsRecord(account, transactionType, toBankCode, toAccountId, amount, accountBalance, memo, txStatus,
					operatorId);
		}
		
		// 帳戶1 扣款
		if( accountBalance.compareTo(amount) >= 0 ) {
			// .subtract() 減法
			accountNewBalance = accountBalance.subtract(amount);
		} else {
			memo = "餘額不足";
			return txService.saveTransactionsRecord(account, transactionType, toBankCode, toAccountId, amount, accountBalance, memo, txStatus,
					operatorId);
		}
		
		// 帳戶2 收款
		toAccountNewBalance = toAccountBalance.add(amount);
		
		
		// 更新兩者帳戶餘額
		int updateRS1 = accountServcie.updateAccountBalance(accountId, accountNewBalance);
		int updateRS2 = accountServcie.updateAccountBalance(toAccountId, toAccountNewBalance);
		
		if(updateRS1 != 1 || updateRS2 !=1) {
			memo = "更新帳戶餘額失敗";
			throw new RuntimeException("更新帳戶餘額失敗，交易回滾");
		}
		txStatus = "轉帳成功";
		
		// 各建立一筆交易紀錄
		txService.saveTransactionsRecord(toAccount, "轉入", toBankCode, accountId, amount, toAccountNewBalance, "對方留言："+memo, txStatus, operatorId);
		
		return txService.saveTransactionsRecord(account, transactionType, toBankCode, toAccountId, amount, accountNewBalance, memo, txStatus, operatorId);
		
	}
	
}
