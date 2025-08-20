package com.bank.fund.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bank.fund.entity.Fund;
import com.bank.fund.entity.FundSip;
import com.bank.fund.service.FundService;
import com.bank.fund.service.FundSipService;

@RestController
@RequestMapping(path = "/fund-sips")
public class FundSipController {
	@Autowired
	private FundSipService fundSipService;

	@GetMapping("/{id}")
	public ResponseEntity<List<FundSip>> getAllFunds(@PathVariable Integer fundAccId) {
		return ResponseEntity.ok(fundSipService.getByFundAccId(fundAccId));
	}

	@PostMapping
	public ResponseEntity<FundSip> createFund(@RequestBody FundSip fundSip) {
		return ResponseEntity.ok(fundSipService.create(fundSip));
	}

	@PutMapping("/{id}")
	public ResponseEntity<FundSip> updateFund(@PathVariable Integer id, @RequestBody FundSip fundSip) {
			return ResponseEntity.ok(fundSipService.update(id, fundSip));
	}

}
