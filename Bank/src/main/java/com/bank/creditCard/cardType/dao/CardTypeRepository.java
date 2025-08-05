package com.bank.creditCard.cardType.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.bank.creditCard.cardType.model.CardTypeBean;

public interface CardTypeRepository extends JpaRepository<CardTypeBean, Integer> {

	@Query("SELECT c.cardType, c.typeCode FROM CardTypeBean c")
	List<Object[]> findIdAndTypeCode();
}
