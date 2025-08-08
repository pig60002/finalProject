package com.bank.creditCard.issue.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bank.creditCard.issue.model.CardDetailBean;

public interface CardDetailRepository extends JpaRepository<CardDetailBean, Integer> {
	
	boolean existsByCardCode(String cardCode);//避免卡號重複
	
	List<CardDetailBean> findByMember_MId(Integer mId);
}
