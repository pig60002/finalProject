package com.bank.creditCard.transaction.dao;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bank.creditCard.issue.model.CardDetailBean;
import com.bank.creditCard.transaction.model.CreditBillBean;

public interface CreditBillRepository extends JpaRepository<CreditBillBean, Integer> {
	boolean existsByCardDetailCardIdAndBillingDate(Integer cardId, LocalDate billingDate);

	@Query(value = """
		    SELECT b.*
		    FROM creditbill b
		    JOIN card_detail c ON c.card_id = b.card_id
		    WHERE c.m_id = :mId
		    ORDER BY b.billing_date DESC
		""", nativeQuery = true)
		List<CreditBillBean> findBillsOfMember(@Param("mId") Integer mId);
	
	// 取得該卡當月帳單（給 attachToBill / 重算用）
    Optional<CreditBillBean> findByCardDetailCardIdAndBillingDate(Integer cardId, LocalDate billingDate);
}
