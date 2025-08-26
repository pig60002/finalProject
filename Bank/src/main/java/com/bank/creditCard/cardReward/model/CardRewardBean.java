package com.bank.creditCard.cardReward.model;

import java.time.LocalDate;

import com.bank.creditCard.issue.model.CardDetailBean;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cardreward")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardRewardBean {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="reward_id")
	private Integer rewardId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "card_id",referencedColumnName = "card_id",nullable = false)
	private CardDetailBean cardDetail;
	
	@Column(name = "points")
	private Integer points;
	
	@Column(name = "earned_date")
	private LocalDate earnedDate;
	
	@Column(name = "expired_date")
	private LocalDate expiredDate;
	
	@Column(name = "description")
	private String description;
	

}
