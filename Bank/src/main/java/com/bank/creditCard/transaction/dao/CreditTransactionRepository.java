package com.bank.creditCard.transaction.dao;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bank.creditCard.transaction.model.CreditTransactionBean;

public interface CreditTransactionRepository extends JpaRepository<CreditTransactionBean, Integer> {
	
	//依卡片ID查詢所有交易
	List<CreditTransactionBean> findByCardDetail_CardId(Integer cardId);
	
	//依會員ID查詢所有交易
	List<CreditTransactionBean> findByMember_MId(Integer mId);
	
	//依卡片ID及交易時間區間查詢交易(用於年月查詢)
//	List<CreditTransactionBean> findByCardDetail_CardIdAndTransactionTimeBetween(
//			Integer cardId,LocalDateTime start,LocalDateTime end);

	List<CreditTransactionBean> findByCardDetailCardIdAndMemberMId(Integer cardId, Integer mId);
	
	// 依卡片ID、會員ID及交易時間區間查詢交易
	List<CreditTransactionBean> findByCardDetailCardIdAndMemberMIdAndTransactionTimeBetween(
	        Integer cardId, Integer mId, LocalDateTime start, LocalDateTime end);
	
	 // 新增：依會員姓名模糊查詢 + 交易時間區間
	@Query("SELECT t FROM CreditTransactionBean t WHERE LOWER(t.member.mName) LIKE LOWER(CONCAT('%', :name, '%')) "
		     + "AND t.transactionTime BETWEEN :start AND :end ORDER BY t.transactionTime DESC")
		List<CreditTransactionBean> findByMemberNameLikeAndTransactionTimeBetween(
		    @Param("name") String name, 
		    @Param("start") LocalDateTime start, 
		    @Param("end") LocalDateTime end);
}
