package com.bank.creditCard.issue.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bank.creditCard.issue.model.CardLimitBean;


public interface CardLimitRepository extends JpaRepository<CardLimitBean, Integer> {
	Optional<CardLimitBean> findByCardDetail_CardIdAndStatus(Integer cardId, String status);
}
