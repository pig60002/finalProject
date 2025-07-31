package com.bank.account.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.bank.account.bean.Transactions;
import com.bank.account.service.TransactionsService;

@RestController
public class TransactionsController {
	
	@Autowired
	private TransactionsService txService;
	
	// 提款or存款
	// Postman : {"accountId":"7100000237","transactionType":"存款","amount":"1000","memo":"07/31"}
	@PutMapping("/account/transaction.controller")
	public Transactions processTransactionsAction(@RequestBody Transactions txRequest) {
		
		return txService.insertTransactionAction(txRequest);
	}
	
}
