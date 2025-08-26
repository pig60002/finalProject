package com.bank.creditCard.payment.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bank.creditCard.dto.PayWithRewardResult;
import com.bank.creditCard.payment.model.CardPaymentBean;
import com.bank.creditCard.payment.service.CardPaymentService;
import com.bank.member.bean.Member;
import com.bank.utils.JwtUtil;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;

@RestController
@RequestMapping("/cardPaymentFront")
public class CardPaymentFrontController {
	
	@Autowired
	private CardPaymentService cardPaymentService;
	
	private Integer getMemberIdFromRequest(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            throw new RuntimeException("未登入");
        }
        String token = header.substring("Bearer ".length()).trim();
        return Integer.parseInt(JwtUtil.getSubject(token));
    }
	
	@Data
    public static class PayReq {
        private Integer creditBillId; // 帳單ID
        private Integer cardId;       // 卡片ID（系統內）
        private String  accountId;    // 付款帳號
        private BigDecimal amount;    // 付款金額
    }
	
	 // 繳費（JSON）
	@PostMapping(
	        value = "/pay",
	        consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE
	    )
	    public CardPaymentBean payCreditCard(
	            @RequestParam("creditBillId") Integer billId,
	            @RequestParam("cardId") Integer cardId,
	            @RequestParam("accountId") String payerAccountId,
	            @RequestParam("amount") BigDecimal payAmount,
	            HttpServletRequest request) {

	        Integer memberId = getMemberIdFromRequest(request);
	        return cardPaymentService.payCreditCard(billId, cardId, memberId, payerAccountId, payAmount);
	    }
	
	//查詢會員付款紀錄
	@GetMapping("/memberPayment")
	public List<CardPaymentBean> getPaymentsByMember(HttpServletRequest request){
		Integer mId = getMemberIdFromRequest(request);
		return cardPaymentService.getPaymentsByMemberId(mId);
	}
	
	//查詢卡片付款紀錄
	@GetMapping("/paymentByCard")
	public List<CardPaymentBean> getPaymentByCard(@RequestParam Integer cardId, HttpServletRequest request){
		Integer mId = getMemberIdFromRequest(request);
		return cardPaymentService.getPaymentsByCardIdAndMemberId(cardId, mId);
	}
	
	@PostMapping(value = "/pay-with-reward", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<PayWithRewardResult> payWithReward(
            @RequestParam("creditBillId") Integer billId,
            @RequestParam("cardId") Integer cardId,
            @RequestParam("accountId") String payerAccountId,
            @RequestParam("amount") BigDecimal plannedPayAmount,                 // 使用者想付多少
            @RequestParam(value = "redeemPoints", required = false) Integer redeemPoints, // 想折抵多少點（可不傳）
            HttpServletRequest request) {

        Integer memberId = getMemberIdFromRequest(request);
        PayWithRewardResult result = cardPaymentService.payWithReward(
                billId, cardId, memberId, payerAccountId, plannedPayAmount, redeemPoints);
        return ResponseEntity.ok(result);
    }
}
