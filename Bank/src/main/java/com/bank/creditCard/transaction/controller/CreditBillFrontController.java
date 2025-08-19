package com.bank.creditCard.transaction.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.bank.creditCard.transaction.model.CreditBillBean;
import com.bank.creditCard.transaction.service.CreditBillService;
import com.bank.utils.JwtUtil;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/creditBillFront")
public class CreditBillFrontController {
	
	@Autowired
	private CreditBillService creditBillService;
	
	private Integer getMemberIdFromRequest(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            throw new RuntimeException("未登入");
        }
        String token = header.substring("Bearer ".length());
        return Integer.parseInt(JwtUtil.getSubject(token));
    }
	//查詢會員帳單(後面換token取mId)
	@GetMapping("/myBills")
	public List<CreditBillBean> findMyBills(HttpServletRequest request) {
		Integer mId = getMemberIdFromRequest(request);
		return creditBillService.findBillsByMemberId(mId);
	}
	
	//查詢帳單細節
	@GetMapping("/billDetail")
	public CreditBillBean getBillDetail(@RequestParam Integer billId, HttpServletRequest request) {
	    Integer mId = getMemberIdFromRequest(request);
	    CreditBillBean bill = creditBillService.findBillDetail(billId);
	    Integer ownerId = bill.getCardDetail().getMember().getmId();
	    if (!ownerId.equals(mId)) {
	        throw new org.springframework.web.server.ResponseStatusException(
	            org.springframework.http.HttpStatus.FORBIDDEN, "無權查看此帳單");
	    }
	    return bill;
	}

}
