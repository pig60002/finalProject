package com.bank.creditCard.transaction.dao;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bank.creditCard.transaction.model.CreditTransactionBean;

public interface CreditTransactionRepository extends JpaRepository<CreditTransactionBean, Integer> {
	
	//依卡片ID查詢所有交易
	List<CreditTransactionBean> findByCardDetail_CardId(Integer cardId);
	
	//依會員ID查詢所有交易
	List<CreditTransactionBean> findByMember_MId(Integer mId);
	
	//依卡片ID及交易時間區間查詢交易(用於年月查詢)
	List<CreditTransactionBean> findByCardDetail_CardIdAndTransactionTimeBetween(
			Integer cardId,LocalDateTime start,LocalDateTime end);

}
