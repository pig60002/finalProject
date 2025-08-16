package com.bank.creditCard.transaction.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bank.creditCard.transaction.model.CreditBillBean;
import com.bank.creditCard.transaction.service.CreditBillService;

@RestController
@RequestMapping("/creditBillBack")
public class CreditBillBackController {
	
	@Autowired
	private CreditBillService creditBillService;
	
	//產生帳單
	@PostMapping("/generateBill")
	public CreditBillBean generateBill(
			@RequestParam("cardId") Integer cardIdInteger,
			@RequestParam("billingYearMonth") String billingYearMonth) {
		return creditBillService.generateBill(cardIdInteger, billingYearMonth);
	}
	
	//查詢所有帳單
	@GetMapping("/getAllBills")
	public List<CreditBillBean> getAllBills(){
		return creditBillService.findAllBills();
	}
	
}
