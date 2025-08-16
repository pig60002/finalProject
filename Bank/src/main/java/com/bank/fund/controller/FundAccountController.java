package com.bank.fund.controller;

import com.bank.fund.entity.FundAccount;
import com.bank.fund.service.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping(path = "/fund-accounts")
public class FundAccountController {

	@Autowired
	private FundAccountService fundAccountService;

	// 查詢基金帳戶
	@GetMapping
	public ResponseEntity<List<FundAccount>> getAllFundAccounts() {
		return ResponseEntity.ok(fundAccountService.getAll());
	}

	// 根據ID查詢單筆基金帳戶
	@GetMapping("/{id}")
	public ResponseEntity<Optional<FundAccount>> getFundAccountById(@PathVariable Integer id) {
		return ResponseEntity.ok(fundAccountService.getById(id));
	}

	// 新增基金帳戶
	@PostMapping
	public ResponseEntity<FundAccount> createFundAccount(@RequestBody FundAccount fundAccount) {
		return ResponseEntity.ok(fundAccountService.create(fundAccount));
	}

	// 更新基金帳戶
	@PutMapping("/{id}")
	public ResponseEntity<FundAccount> updateFundAccount(@PathVariable Integer id, @RequestBody FundAccount fundAccount) {
			return ResponseEntity.ok(fundAccountService.update(id, fundAccount));
	}

}
