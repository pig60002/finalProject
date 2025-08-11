package com.bank.creditCard.transaction.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bank.creditCard.transaction.model.CreditTransactionBean;
import com.bank.creditCard.transaction.service.CreditTransactionService;

@RestController
@RequestMapping("/transactionBack")
public class CreditTransactionBackController {
	
	@Autowired
	private CreditTransactionService creditTransactionService;
	
	//依會員ID cardID 年月查詢
	@GetMapping("/searchByMonth")
	public List<CreditTransactionBean> getTransactionsByCardMemberAndMonth(
			@RequestParam("cardId") Integer cardId,
			@RequestParam("mId") Integer mId,
			@RequestParam("yearMonth") String yearMonth
			){
		return creditTransactionService.findByCardIdAndMemberIdAndBillingYearMonth(cardId, mId, yearMonth);
	}

	//退款API
	@PostMapping("/refund")
	public CreditTransactionBean refundTransaction(@RequestParam("transactionId") Integer transactionId) {
		return creditTransactionService.refundTransaction(transactionId);
	}
	
}
