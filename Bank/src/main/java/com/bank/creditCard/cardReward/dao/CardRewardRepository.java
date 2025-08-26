package com.bank.creditCard.cardReward.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.bank.creditCard.cardReward.model.CardRewardBean;
@Repository
public interface CardRewardRepository extends JpaRepository<CardRewardBean, Integer> {
	
	boolean existsByCardDetail_CardIdAndDescription(Integer cardId,String description);
	
	@Query("""
			SELECT COALESCE(SUM(r.points),0)
			FROM CardRewardBean r
			WHERE r.cardDetail.cardId=:cardId
			""")
	Integer sumByCardId(@Param("cardId") Integer cardId);

}
