package com.bank.creditCard.cardType.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity @Table(name = "card_type")
@Data
public class CardTypeBean {
	
	@Id@Column(name = "card_type_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int cardType;
	
	@Column(name = "type_code")
	private String typeCode;
	@Column(name = "type_name")
	private String typeName;
	@Column(name = "default_limit")
	private BigDecimal defaultLimit;
	@Column(name = "logo_url")
	private String logoUrl;
	@Column(name = "cashback_rate")
	private BigDecimal cashbackRate;
	@Column(name = "use_custom_rule")
	private Boolean useCustomRule;
	public CardTypeBean() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	
}
