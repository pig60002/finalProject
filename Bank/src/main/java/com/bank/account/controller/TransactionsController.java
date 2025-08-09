package com.bank.account.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bank.account.bean.Transactions;
import com.bank.account.service.transactions.DepositWithdrawalTxService;
import com.bank.account.service.transactions.InternalTransferService;
import com.bank.account.service.transactions.TransactionsService;

@RestController
public class TransactionsController {
	
	@Autowired
	private TransactionsService transactionsService;
	
	@Autowired
	private DepositWithdrawalTxService dwTxSerice;
	
	@Autowired
	private InternalTransferService interTxService;
	
	// 提款or存款
	// Postman : {"accountId":"7100000237","transactionType":"存款","amount":"1000","memo":"07/31"}
	@PutMapping("/account/transaction/depositwithdrawal")
	public Transactions processDepositWithdrawalAction(@RequestBody Transactions txRequest) {
		
		return dwTxSerice.depositWithdrawalAction(txRequest);
	}
	
	// 內部轉帳
	// Postman :{"accountId":"7100000028","transactionType":"內部轉帳","toAccountId":"7100000237","amount":"1000","operatorId":1}
	@PutMapping("/account/transaction/internaltransfer")
	public Transactions processInternalTransferAction(@RequestBody Transactions txRequest) {
		
		try {
			
			return interTxService.internalTransferAction(txRequest);
			
		} catch (RuntimeException e) {

			Transactions errorTx = new Transactions();
			errorTx.setStatus("交易失敗");
			errorTx.setMemo("交易錯誤:"+ e.getMessage());
			return errorTx;
		}
		
	}
	
	// 依帳戶號碼查尋交易明細
	@GetMapping("/account/transaction/gettransactionsrecords")
	public List<Transactions> processGetTransactionsRecordAction(@RequestParam String accountId){
		
		return transactionsService.getTransactionByAccountId(accountId);
	}
	
	// 查詢帳戶所有"成功"交易
	@GetMapping("/account/transaction/getsuccesstxrecords.controller")
	public List<Transactions> processTxSuccessRecordAction(@RequestParam String accountId){
		
		return transactionsService.getTxSuccessRecords(accountId);
	}
	
}
