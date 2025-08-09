package com.bank.account.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
	
	@Autowired
	private SerialControlService scService;
	
	// 獲得個人所有帳戶
	public List<Account> getAccountsByMId(Integer mid){
		return accountRepos.findByMId(mid);
	}
	
	// 依帳號查詢帳戶
	public Account getByAccountId(String accountId) {
		return accountRepos.findByAccountId(accountId);
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
	
	// 新增帳戶
	public Account insertAccount(Integer mid, String accountName, String currency ) {
		Account insertBean = new Account();
		
		// 獲得帳戶流水號
		String accountid = scService.getSCNB("account", "100");
		
		insertBean.setAccountId(accountid);
		insertBean.setmId(mid);
		insertBean.setAccountName(accountName);
		insertBean.setCurrency(currency);
		insertBean.setBalance(BigDecimal.ZERO);
		insertBean.setOpenedDate(LocalDate.now());
		insertBean.setStatus("啟用");
		
		return accountRepos.save(insertBean);
	}
	
	// 修改帳戶狀態
	public int updateAccountStatus(String status, String memo, Integer operatorId, String accountId) {
		int updateRS = accountRepos.updateAccountStatus(status, memo, operatorId, LocalDateTime.now(), accountId);
		
		if(updateRS > 0) {
			System.out.println("修改帳戶狀態成功");
		} else {
			System.out.println("修改帳戶狀態失敗");
		}
		
		return updateRS;
	}
	
	// 修改帳戶餘額
	public int updateAccountBalance(String accountId , BigDecimal newBalance) {
		return accountRepos.updateAccountBalance(newBalance, LocalDate.now(), accountId);
	}
	
	// 多欄位查詢帳戶
	public List<Account> searchAccounts(Integer mId, String mIdentity, String mPhone,String mName,String accountId){
		return accountRepos.searchAccounts(mId, mIdentity, mPhone, mName, accountId);
	}
	
	// 搜尋所有帳戶
	public List<Account> findAllAccounts(){
		return accountRepos.findAll();
	}
	
}
