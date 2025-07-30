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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bank.account.bean.Account;
import com.bank.account.service.AccountServcie;

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
	
	// 修改帳戶-餘額 (提款)
	
	
}
