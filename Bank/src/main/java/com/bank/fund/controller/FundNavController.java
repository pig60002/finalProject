package com.bank.fund.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bank.fund.entity.FundNav;
import com.bank.fund.service.FundNavService;


@RestController
@RequestMapping(path = "/fundNav")
public class FundNavController {
	
	@Autowired
	private FundNavService fundNavService;

	@GetMapping
	public ResponseEntity<List<FundNav>> getFundNavsByFundId(@RequestParam Integer fundId) {
		return ResponseEntity.ok(fundNavService.getByFundId(fundId));
	}

	@GetMapping("/{id}")
	public ResponseEntity<FundNav> getFundNavById(@PathVariable Integer id) {
		return ResponseEntity.ok(fundNavService.getById(id).orElseThrow());
	}

	@PostMapping
	public ResponseEntity<FundNav> createFundNav(@RequestBody FundNav fundNav) {
		return ResponseEntity.ok(fundNavService.create(fundNav));
	}



}
