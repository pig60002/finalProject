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

	@GetMapping
	public ResponseEntity<List<FundSip>> getAllFunds() {
		return ResponseEntity.ok(fundSipService.getAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<Optional<FundSip>> getFundById(@PathVariable Integer id) {
		return ResponseEntity.ok(fundSipService.getById(id));
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
