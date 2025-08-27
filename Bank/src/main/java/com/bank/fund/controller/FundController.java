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

import com.bank.fund.dto.FundDto;
import com.bank.fund.entity.Fund;
import com.bank.fund.service.FundService;

@RestController
@RequestMapping(path = "/fund")
public class FundController {
	
	@Autowired
	private FundService fundService;

	@GetMapping
	public ResponseEntity<List<FundDto>> getAllFunds() {
		return ResponseEntity.ok(fundService.getAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<Fund> getFundById(@PathVariable Integer id) {
		return ResponseEntity.ok(fundService.getById(id).orElseThrow());
	}

	@PostMapping
	public ResponseEntity<Fund> createFund(@RequestBody Fund fund) {
		return ResponseEntity.ok(fundService.create(fund));
	}

	@PutMapping("/{id}")
	public ResponseEntity<Fund> updateFund(@PathVariable Integer id, @RequestBody Fund fund) {
			return ResponseEntity.ok(fundService.update(id, fund));
	}

}
