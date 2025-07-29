package com.bank.account.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bank.account.bean.Account;
import com.bank.account.dao.AccountRepository;

@Service
@Transactional
public class AccountServcie {
	
	@Autowired
	private AccountRepository accountRepos;
	
	// 獲得個人所有帳戶
	public List<Account> getAccountsByMId(Integer mid){
		return accountRepos.findByMId(mid);
	}
	
	// 獲得個人的資產總額(個人所有帳戶的加總)
	public BigDecimal getTotalBalance(Integer mid) {
		
		BigDecimal total = new BigDecimal(0);
		
		List<Account> accountLists = getAccountsByMId(mid);
		for(Account acc : accountLists) {
			BigDecimal balance = acc.getBalance();
			total = total.add(balance);
		}
		System.out.println(total);
		
		return total;
	}
	
	// 
	
	
}
