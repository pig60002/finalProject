package com.bank.creditCard.payment.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bank.creditCard.payment.model.CardPaymentBean;

public interface CardPaymentRepository extends JpaRepository<CardPaymentBean, Integer>{

	List<CardPaymentBean> findByCardId(Integer cardId);
    List<CardPaymentBean> findBymId(Integer mId);
	
}
