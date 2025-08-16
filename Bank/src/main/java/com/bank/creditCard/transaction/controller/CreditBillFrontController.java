package com.bank.creditCard.transaction.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.bank.creditCard.transaction.model.CreditBillBean;
import com.bank.creditCard.transaction.service.CreditBillService;

@RestController
@RequestMapping("/creditBillFront")
public class CreditBillFrontController {
	
	@Autowired
	private CreditBillService creditBillService;
	
	//查詢會員帳單(後面換token取mId)
	@GetMapping("/myBills")
	public List<CreditBillBean> findMyBill(Integer mId) {
		return creditBillService.findBillsByMemberId(mId);
	}
	
	//查詢帳單細節
	@GetMapping("/billsDetail")
	public CreditBillBean getBillDetail(Integer billId) {
		return creditBillService.findBillDetail(billId);
	}

}
