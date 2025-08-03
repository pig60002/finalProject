package com.bank.creditCard.application.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.bank.creditCard.application.model.CardApplicationBean;

@Repository
public interface CardApplicationRepository extends JpaRepository<CardApplicationBean, Integer> {
	
	List<CardApplicationBean> findByUserId(int userId);
	
	List<CardApplicationBean> findByStatusOrderByApplyDateDesc(String status);
	
	List<CardApplicationBean> findByCardTypeOrderByApplyDateDesc(int cardType);
	
	@Query("""
		SELECT a FROM CardApplicationBean a
		JOIN FETCH a.member m
		WHERE a.status= :status
		ORDER BY a.applyDate ASC			
	""")
	List<CardApplicationBean> findWithMemberByStatus(String status);
	

}
