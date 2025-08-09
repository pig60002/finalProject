package com.bank.creditCard.application.model;


import java.sql.Date;
import java.time.LocalDateTime;

import com.bank.member.bean.Member;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;

@Entity @Table(name="card_application")
@AllArgsConstructor
@Data
public class CardApplicationBean {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "application_id")
	private int applicationId;
	
	@Column(name = "user_id")
	private int userId;
	
	@ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "user_id",insertable = false,updatable = false)
	private Member member;
	
	@Column(name = "card_type")
	private int cardType;
	@Column(name = "id_photo_front_url")
	private String idPhotoFront;
	@Column(name = "id_photo_back_url")
	private String idPhotoBack;
	@Column(name = "financial_proof_url")
	private String financialProof;
	@Column(name ="apply_date")
	private LocalDateTime applyDate;
	@Column(name = "review_date")
	private LocalDateTime reviewDate;
	@Column(name = "status")
	private String status;
	public static final String STATUS_PENDING = "PENDING";
    public static final String STATUS_APPROVED = "APPROVED";
    public static final String STATUS_REJECTED = "REJECTED";
    public static final String STATUS_ISSUED = "ISSUED";
    @Column(name = "review_comment")
    private String reviewComment;
    
	public CardApplicationBean() {
		super();
		// TODO Auto-generated constructor stub
	}	
	
}
