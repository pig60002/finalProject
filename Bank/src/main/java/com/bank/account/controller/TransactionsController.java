package com.bank.account.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.bank.account.bean.Transactions;
import com.bank.account.service.transactions.DepositWithdrawalTxService;
import com.bank.account.service.transactions.InternalTransferService;

@RestController
public class TransactionsController {
	
	@Autowired
	private DepositWithdrawalTxService dwTxSerice;
	
	@Autowired
	private InternalTransferService interTxService;
	
	// 提款or存款
	// Postman : {"accountId":"7100000237","transactionType":"存款","amount":"1000","memo":"07/31"}
	@PutMapping("/account/transaction/depositwithdrawal.controller")
	public Transactions processDepositWithdrawalAction(@RequestBody Transactions txRequest) {
		
		return dwTxSerice.depositWithdrawalAction(txRequest);
	}
	
	@PutMapping("/account/transaction/internaltransfer.controller")
	// 內部轉帳
	// Postman :{"accountId":"7100000028","transactionType":"內部轉帳","toAccountId":"7100000237","amount":"1000","operatorId":1}
	public Transactions processInternalTransferAction(@RequestBody Transactions txRequest) {
		
		return interTxService.internalTransferAction(txRequest);
	}
	
}
