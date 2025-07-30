package com.bank.loan.controller;

import org.springframework.web.bind.annotation.RestController;

import com.bank.loan.bean.Loans;
import com.bank.loan.service.LoansService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;


@RestController
public class LoansController {
	
	@Autowired
	private LoansService lService;
	
	@PostMapping("/loans.controller")
	public List<Loans> LoansAction() {
		return lService.findAlldata();
	}
	

}
