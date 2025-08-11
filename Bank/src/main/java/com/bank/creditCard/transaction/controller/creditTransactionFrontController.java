package com.bank.creditCard.transaction.controller;
import com.bank.loan.service.CreditReviewDtoService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bank.creditCard.dto.CreditTransactionDTO;
import com.bank.creditCard.transaction.model.CreditTransactionBean;
import com.bank.creditCard.transaction.service.CreditTransactionService;

@RestController
@RequestMapping("/transactionFront")
public class creditTransactionFrontController {

	@Autowired
	private CreditTransactionService creditTransactionService;
	
	//新增交易
    @PostMapping("/addTransaction")
	public CreditTransactionBean createTransaction(@RequestBody CreditTransactionDTO dto ) {
		return creditTransactionService.addTransaction(dto);
	}
    
    //查詢會員所有交易
    @GetMapping("/getTransactionByMember")
    public List<CreditTransactionBean> getTransactionByMember(@RequestParam("mId") Integer mId){
    	return creditTransactionService.getTransactionsByMemberId(mId);
    }
    
    //查詢某張卡的交易
    @GetMapping("/getTransactionsByCard")
    public List<CreditTransactionBean> getTransactionsByCard(
        @RequestParam("cardId") Integer cardId,
        @RequestParam("mId") Integer mId
    ){
        return creditTransactionService.getTransactionsByCardIdAndMemberId(cardId, mId);
    }
    
}

