package com.bank.creditCard.transaction.dao;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bank.creditCard.issue.model.CardDetailBean;
import com.bank.creditCard.transaction.model.CreditBillBean;

public interface CreditBillRepository extends JpaRepository<CreditBillBean, Integer> {
	boolean existsByCardDetailCardIdAndBillingDate(Integer cardId, LocalDate billingDate);

	List<CreditBillBean> findByCardDetailMemberMId(Integer memberId);
}
