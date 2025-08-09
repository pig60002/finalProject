package com.bank.creditCard.issue.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bank.creditCard.issue.model.CardDetailBean;
import com.bank.creditCard.issue.service.CardIssueService;

import jakarta.persistence.Entity;

@RestController
@RequestMapping("/memberCard")
public class CardIssueFrontController {

	@Autowired
	private CardIssueService cardIssueService;
	
	//查會員卡片
	@GetMapping("/getCard")
	public List<CardDetailBean> getCardByMId(@RequestParam("mId") Integer mId){
		return cardIssueService.getCardByMemberId(mId);
	}
	
	//開卡
	@PostMapping("/status")
	public ResponseEntity<String> updateCardStatus(@RequestParam("cardId") Integer cardId,
											   @RequestParam("action") String action){
		 boolean success = cardIssueService.updateCardStatus(cardId, action);
		 return success ? ResponseEntity.ok("卡片狀態已更新")
		                : ResponseEntity.badRequest().body("卡片狀態更新失敗或不合法");
	}
}
