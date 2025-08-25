package com.bank.fund.controller;

import com.bank.fund.entity.FundAccount;
import com.bank.fund.service.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping(path = "/fundAccount")
public class FundAccountController {

	@Autowired
	private FundAccountService fundAccountService;

	@GetMapping
	public ResponseEntity<List<FundAccount>> getAllFundAccounts() {
		return ResponseEntity.ok(fundAccountService.getAll());
	}
	
	@GetMapping(params = "status")
	public ResponseEntity<List<FundAccount>> getFundAccountsByStatus(@RequestParam String status) {
		return ResponseEntity.ok(fundAccountService.getByStatus(status));
	}

	@GetMapping("/{id}")
	public ResponseEntity<Optional<FundAccount>> getFundAccountById(@PathVariable Integer id) {
		return ResponseEntity.ok(fundAccountService.getById(id));
	}
	
	//查詢會員的基金帳戶，沒有則傳204讓前端顯示[申辦基金帳戶]
	@GetMapping(params = "mId")
	public ResponseEntity<Optional<FundAccount>> getFundAccountByMId(@RequestParam Integer mId){
		Optional<FundAccount> fundAccount = fundAccountService.getByMId(mId);
		if(fundAccount.isPresent())
			return ResponseEntity.ok(fundAccount);
		else
			return ResponseEntity.noContent().build();
	}
	
	@PostMapping("/riskAnswers")
	public ResponseEntity<String> createRiskType(@RequestBody List<Integer> riskAnswers){
		return ResponseEntity.ok(fundAccountService.createRiskType(riskAnswers));
	}

	@PostMapping
	public ResponseEntity<FundAccount> createFundAccount(@RequestBody FundAccount fundAccount) {
		return ResponseEntity.ok(fundAccountService.create(fundAccount));
	}

	@PutMapping("/{id}")
	public ResponseEntity<FundAccount> updateFundAccount(@PathVariable Integer id, @RequestBody FundAccount fundAccount) {
			return ResponseEntity.ok(fundAccountService.update(id, fundAccount));
	}

}
