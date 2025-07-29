package com.bank.account.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.bank.account.bean.Account;
import com.bank.account.service.AccountServcie;

@RestController
public class AccountController {
	
	@Autowired
	private AccountServcie accountServcie;
	
	// 獲得個人所有帳戶   POSTMAN 還沒測完
	
	@GetMapping("/account/getmemberaccounts/{mid}")
	public List<Account> processGetAccountByMIdAction(@PathVariable Integer mid){
		return accountServcie.getAccountsByMId(mid);
	}
	
	
}
