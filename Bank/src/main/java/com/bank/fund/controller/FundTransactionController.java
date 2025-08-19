package com.bank.fund.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bank.fund.entity.FundTransaction;
import com.bank.fund.service.FundTransactionService;

@RestController
@RequestMapping(path = "/fund-transactions")
public class FundTransactionController {
	
	@Autowired
	private FundTransactionService fundTransactionService;
	
	@GetMapping("/{fundAccId}")
	public ResponseEntity<List<FundTransaction>> getFundTransactionsByFundAccId(@PathVariable Integer fundAccId) {
		return ResponseEntity.ok(fundTransactionService.getByFundAccId(fundAccId));
	}

	@PostMapping("/buy")
	public ResponseEntity<FundTransaction> buyFund(@RequestBody FundTransaction fundTransaction) {
		return ResponseEntity.ok(fundTransactionService.buyFund(fundTransaction));
	}
	
	@PostMapping("/sell")
	public ResponseEntity<FundTransaction> sellFund(@RequestBody FundTransaction fundTransaction) {
		return ResponseEntity.ok(fundTransactionService.sellFund(fundTransaction));
	}

	@PutMapping("/buy/{id}")
	public ResponseEntity<FundTransaction> agreeBuyFund(@PathVariable Integer id, @RequestBody FundTransaction fundTransaction) {
			return ResponseEntity.ok(fundTransactionService.agreeBuyFund(id, fundTransaction));
	}

	@PutMapping("/sell/{id}")
	public ResponseEntity<FundTransaction> agreeSellFund(@PathVariable Integer id, @RequestBody FundTransaction fundTransaction) {
		return ResponseEntity.ok(fundTransactionService.agreeSellFund(id, fundTransaction));
	}
}
