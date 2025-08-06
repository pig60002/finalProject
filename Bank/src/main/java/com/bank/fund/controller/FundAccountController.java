package com.bank.fund.controller;

import com.bank.fund.dto.FundAccountDto;
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
	public ResponseEntity<List<FundAccountDto>> getFundAccounts(
			@RequestParam(required = false) String status,
			@RequestParam(required = false) String name
			) {
				
		return ResponseEntity.ok(fundAccountService.getFundAccounts(status, name));
	}

	//根據ID查詢單筆基金帳戶
	@GetMapping("/{id}")
	public ResponseEntity <Optional<FundAccount> > getFundAccountById(@PathVariable Integer id) {

		return ResponseEntity.ok(fundAccountService.getById(id));
	}

	// 新增基金帳戶
	@PostMapping
	public ResponseEntity<String> createFundAccount(
			@RequestParam Integer memberId, 
			@RequestParam String riskType) {
		boolean success = fundAccountService.create(memberId, riskType);

		if (success) {
			return ResponseEntity.status(HttpStatus.CREATED).body("新增成功");
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("新增失敗");
		}
	}

	// 更新基金帳戶
	@PatchMapping("/{id}")
	public ResponseEntity<String> updateFundAccount(
			@PathVariable Integer id,
			@RequestParam(required = false) String riskType,
			@RequestParam(required = false) String status) {

		boolean success = fundAccountService.update(id,riskType, status);

		if (success) {
			return ResponseEntity.ok("更新成功");
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("更新失敗");
		}
	}

	// 刪除基金帳戶
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteFundAccount(@PathVariable Integer id) {
		boolean success = fundAccountService.delete(id);
		if (success) {
			return ResponseEntity.ok("刪除成功");
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("刪除失敗");
		}
	}
}
