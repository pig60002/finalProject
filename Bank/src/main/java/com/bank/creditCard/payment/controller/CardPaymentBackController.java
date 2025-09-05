package com.bank.creditCard.payment.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.bank.creditCard.payment.model.CardPaymentBean;
import com.bank.creditCard.payment.service.CardPaymentService;

@RestController
@RequestMapping("/cardPaymentBack")
public class CardPaymentBackController {

	@Autowired
	private CardPaymentService cardPaymentService;
	
	//查單一付款紀錄
	@GetMapping("/getPaymentDetail")
	public CardPaymentBean getPaymentById(@RequestParam("paymentId") Integer paymentId) {
		return cardPaymentService.getPaymentById(paymentId).orElse(null);
	}
	
	//查詢某卡付款紀錄
	@GetMapping("/getPaymentByCard")
	public List<CardPaymentBean> getPaymentByCard(@RequestParam("cardId") Integer cardId){
		return cardPaymentService.getPaymentsByCardId(cardId);
	}
	
	//更新付款狀態(人工補單調整用)
	@PostMapping("/updateStatus")
	public CardPaymentBean updatePaymentStatus(@RequestParam("paymentId") Integer paymentId,@RequestParam("status") String status) {
		return cardPaymentService.updatePaymentStatus(paymentId, status);
	}
	
	//計算某卡累積金額
	@GetMapping("/totalPaid")
	public BigDecimal getTotalPaidByCard(@RequestParam("cardId") Integer cardId) {
		return cardPaymentService.getTotalPaidByCardId(cardId);
	}

}
