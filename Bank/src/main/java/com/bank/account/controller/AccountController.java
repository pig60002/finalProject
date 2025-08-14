package com.bank.account.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.bank.account.bean.Account;
import com.bank.account.service.AccountServcie;
import com.bank.member.bean.Member;

@RestController
public class AccountController {
	
	@Autowired
	private AccountServcie accountServcie;
	
	// 獲得個人所有帳戶  **未來JWT要改接(HttpServletRequest request)**
	@GetMapping("/account/getmemberaccounts/{mid}")
	public List<Account> processGetAccountByMIdAction(@PathVariable Integer mid){
		return accountServcie.getAccountsByMId(mid);
	}
	
	// 新增帳戶
	@PostMapping("/account/insert")
	// Postman 測試 {"mId":1000,"accountName":"保險費","currency":"NT"}
	public ResponseEntity<String> processInsertAccountAction(@RequestBody Account acc) {
		
		Account insertRS = accountServcie.insertAccount(
				acc.getmId(), 
				acc.getAccountName(), 
				acc.getCurrency()
		);
		if( insertRS != null ) {
			return ResponseEntity.ok("新增成功");
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("新增失敗");
		}
	}
	
	// 修改帳戶-狀態
	@PutMapping("/account/update")
	public ResponseEntity<String> processUpdateAccountAction(@RequestBody Account acc) {
		int updateRS = accountServcie.updateAccountStatus(
				acc.getStatus(), 
				acc.getMemo(), 
				acc.getOperatorId(), 
				acc.getAccountId()
		);
		
		if( updateRS > 0 ) {
			return ResponseEntity.ok("更新狀態成功");
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("更新狀態失敗");
		}
	}
	
	// 多欄位查詢帳戶
	@PostMapping("/account/searchaccount")
	public List<Account> processSearchAccountsAction(@RequestBody Account acc){
		Member member = acc.getMember();
		System.out.println(member);
		return accountServcie.searchAccounts(acc.getmId()
				, member != null ? member.getmIdentity() : null
				,member != null ? member.getmPhone() : null
				,member != null ? member.getmName() : null
				, acc.getAccountId());
	}
	
	// 查詢所有帳戶
	@PostMapping("/account/searchallaccount")
	public List<Account> processSearchAllAccount(){
		return accountServcie.findAllAccounts();
	}
	
	
}
