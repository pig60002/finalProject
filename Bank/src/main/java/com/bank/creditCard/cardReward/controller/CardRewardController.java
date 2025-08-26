package com.bank.creditCard.cardReward.controller;

import java.util.Map;

import javax.management.RuntimeErrorException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bank.creditCard.cardReward.service.CardRewardService;
import com.bank.utils.JwtUtil;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/reward")
public class CardRewardController {
	@Autowired
	private CardRewardService cardRewardService;
	
	private Integer getMemberId(HttpServletRequest request) {
		String header=request.getHeader("Authorization");
		if(header==null||!header.startsWith("Bearer"))throw new RuntimeException("未登入");
		String token=header.substring("Bearer".length()).trim();
		return Integer.parseInt(JwtUtil.getSubject(token));
	}
	
	//查可用紅利點數
	@GetMapping("/points")
	public Map<String,Object> points(@RequestParam Integer cardId,HttpServletRequest request){
		int pts=cardRewardService.getAvailablePoints(getMemberId(request), cardId);
		return Map.of("cardId",cardId,"points",pts);
	}
	
	//折抵
//	@PostMapping("/redeem")
//	public ResponseEntity<?> redeem(@RequestParam Integer cardId,
//									@RequestParam Integer points,
//									@RequestParam(required = false)String billId,
//									HttpServletRequest	request){
//		int remain=cardRewardService.redeem(getMemberId(request), cardId, points, billId);
//		return ResponseEntity.ok(Map.of("cardId",cardId,"remainPoints",remain));
//	}

}
