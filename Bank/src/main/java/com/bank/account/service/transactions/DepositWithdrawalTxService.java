package com.bank.account.service.transactions;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bank.account.bean.Account;
import com.bank.account.bean.Transactions;
import com.bank.account.service.AccountServcie;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class DepositWithdrawalTxService {

	@Autowired
	private TransactionsService txService;
	
	@Autowired
	private AccountServcie accountServcie;

	// 帳戶提款、存款
	public Transactions depositWithdrawalAction(Transactions txRequest) {

		String accountId = txRequest.getAccountId();
		BigDecimal amount = txRequest.getAmount();
		String transactionType = txRequest.getTransactionType();
		String memo = txRequest.getMemo();
		Integer operatorId = txRequest.getOperatorId();

		String txStatus = "交易失敗"; // 預設交易失敗

		// 查詢帳戶
		Account accountRS = accountServcie.getByAccountId(accountId);
		if (accountRS == null) {
			memo = "帳戶不存在";
			return txService.saveTransactionsRecord(null, transactionType, null, null, null, memo, txStatus,
					operatorId);
		}

		BigDecimal balance = accountRS.getBalance();
		BigDecimal newBalance = null;

		// 檢查交易金額
		if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
			memo = "交易金額輸入錯誤";
			return txService.saveTransactionsRecord(accountRS, transactionType, null, null, balance, memo, txStatus,
					operatorId);
		}

		switch (transactionType) {

		case "提款":

			if (balance.compareTo(amount) >= 0) {
				// .subtract() 減法
				newBalance = balance.subtract(amount);
				txStatus = "交易成功";

			} else {
				memo = "餘額不足";
				return txService.saveTransactionsRecord(accountRS, transactionType, null, null, balance, memo, txStatus,
						operatorId);
			}
			break;

		case "存款":
			// .add() 加法
			newBalance = balance.add(amount);
			txStatus = "交易成功";
			break;
		default:

			memo = "交易類型錯誤";
			return txService.saveTransactionsRecord(accountRS, transactionType, null, null, balance, memo, txStatus,
					operatorId);
		}

		// 更新帳號餘額
		if ("交易成功".equals(txStatus)) {

			int updateRS = accountServcie.updateAccountBalance(accountId, newBalance);

			if (updateRS != 1) {
				txStatus = "交易失敗";
				memo = "更新帳戶餘額失敗";
				return txService.saveTransactionsRecord(accountRS, transactionType, null, null, balance, memo, txStatus,
						operatorId);
			}
		}

		// 建立交易紀錄
		return txService.saveTransactionsRecord(accountRS, transactionType, null, null,
				newBalance != null ? newBalance : balance, memo, txStatus, operatorId);
	}

}
