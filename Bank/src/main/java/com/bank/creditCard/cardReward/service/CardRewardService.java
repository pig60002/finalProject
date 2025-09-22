package com.bank.creditCard.cardReward.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bank.creditCard.cardReward.dao.CardRewardRepository;
import com.bank.creditCard.cardReward.model.CardRewardBean;
import com.bank.creditCard.issue.dao.CardDetailRepository;
import com.bank.creditCard.issue.model.CardDetailBean;



@Service
public class CardRewardService {
	
	@Autowired
	private CardRewardRepository cardRewardRepository;
	
	@Autowired
	private CardDetailRepository cardDetailRepository;
	
	//驗證cardId屬於該會員
	private CardDetailBean mustOwnCard(Integer memberId,Integer cardId) {
		CardDetailBean card = cardDetailRepository.findById(cardId)
				.orElseThrow(()->new IllegalArgumentException("卡片不存在"));
		if(card.getMember()==null||!memberId.equals(card.getMember().getmId())) {
			throw new IllegalArgumentException("您無權存取此卡片");
		}
		return card;
	}
	
	//目前可用點數(正負累積)
	@Transactional(readOnly=true)
	public int getAvailablePoints(Integer memberId,Integer cardId) {
		mustOwnCard(memberId, cardId);
		return cardRewardRepository.sumByCardId(cardId);
	}
	
	//交易入帳後 新增紅利表
	@Transactional
	public void earnFromTransaction(Integer cardId,int points,String txnIdOrMemo) {
		if(points==0)return;
		CardDetailBean card = cardDetailRepository.findById(cardId).orElseThrow(()->new IllegalArgumentException("卡片不存在"));
		
		String desc=(txnIdOrMemo!=null)?"TXN:"+txnIdOrMemo:"TXN";
		//同一交易不重複入點
		if(cardRewardRepository.existsByCardDetail_CardIdAndDescription(cardId, desc))return;
		
		CardRewardBean row = new CardRewardBean();
		row.setCardDetail(card);
		row.setPoints(points);
		row.setEarnedDate(LocalDate.now());
		row.setExpiredDate(null);
		row.setDescription(desc);
		cardRewardRepository.save(row);
	}
	
	//折抵帳單
	@Transactional
	public int redeem(Integer memberId,Integer cardId,int redeemPoints,String billId) {
		if(redeemPoints<=0)throw new IllegalArgumentException("扣點必須為正數");
		
		CardDetailBean card = mustOwnCard(memberId, cardId);
		
		int available=cardRewardRepository.sumByCardId(cardId);
		if(available<redeemPoints) {
			throw new IllegalArgumentException("點數不足,無法折抵");
		}
		
		String desc=(billId != null) ? "REDEEM:BILL:" + billId : "REDEEM";
		//防重
		if (cardRewardRepository.existsByCardDetail_CardIdAndDescription(cardId, desc)) {
            return available; 
        }
		
		CardRewardBean row = new CardRewardBean();
		row.setCardDetail(card);
		row.setPoints(-redeemPoints);
		row.setEarnedDate(LocalDate.now());
		row.setExpiredDate(null);
		row.setDescription(desc);
		cardRewardRepository.save(row);
		
		return available-redeemPoints;
	}
	

}
