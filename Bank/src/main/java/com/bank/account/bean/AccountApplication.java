package com.bank.account.bean;

import java.time.LocalDateTime;

import com.bank.member.bean.Member;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity @Table(name="account_application")
//@Component
public class AccountApplication {
	
	@Id @Column(name="application_id")
	private String applicationId;
	
	@Column(name="m_id")
	private Integer mId;
	
	@Column(name="id_card_front")
	private String idCardFront;
	
	@Column(name="id_card_back")
	private String idCardBack;
	
	@Column(name="second_doc")
	private String secondDoc;
	
	@Column(name="status")
	private String status;
	
	@Column(name="reviewer_id")
	private Integer reviewerId;
	
	@Column(name="review_time")
	private LocalDateTime reviewTime;
	
	@Column(name="rejection_reason")
	private String rejectionReason;
	
	@Column(name="apply_time")
	private LocalDateTime applyTime;
	
	@ManyToOne @JoinColumn(name="m_id", insertable = false, updatable = false)
	private Member member;

	public AccountApplication() {
	}

	public AccountApplication(String applicationId, Integer mId, String idCardFront, String idCardBack,
			String secondDoc, String status, Integer reviewerId, LocalDateTime reviewTime, String rejectionReason,
			LocalDateTime applyTime, Member member) {
		super();
		this.applicationId = applicationId;
		this.mId = mId;
		this.idCardFront = idCardFront;
		this.idCardBack = idCardBack;
		this.secondDoc = secondDoc;
		this.status = status;
		this.reviewerId = reviewerId;
		this.reviewTime = reviewTime;
		this.rejectionReason = rejectionReason;
		this.applyTime = applyTime;
		this.member = member;
	}

	public AccountApplication(Integer mId, String idCardFront, String idCardBack, String secondDoc, String status,
			Integer reviewerId, LocalDateTime reviewTime, String rejectionReason, LocalDateTime applyTime,
			Member member) {
		super();
		this.mId = mId;
		this.idCardFront = idCardFront;
		this.idCardBack = idCardBack;
		this.secondDoc = secondDoc;
		this.status = status;
		this.reviewerId = reviewerId;
		this.reviewTime = reviewTime;
		this.rejectionReason = rejectionReason;
		this.applyTime = applyTime;
		this.member = member;
	}

	public String getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	public Integer getmId() {
		return mId;
	}

	public void setmId(Integer mId) {
		this.mId = mId;
	}

	public String getIdCardFront() {
		return idCardFront;
	}

	public void setIdCardFront(String idCardFront) {
		this.idCardFront = idCardFront;
	}

	public String getIdCardBack() {
		return idCardBack;
	}

	public void setIdCardBack(String idCardBack) {
		this.idCardBack = idCardBack;
	}

	public String getSecondDoc() {
		return secondDoc;
	}

	public void setSecondDoc(String secondDoc) {
		this.secondDoc = secondDoc;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getReviewerId() {
		return reviewerId;
	}

	public void setReviewerId(Integer reviewerId) {
		this.reviewerId = reviewerId;
	}

	public LocalDateTime getReviewTime() {
		return reviewTime;
	}

	public void setReviewTime(LocalDateTime reviewTime) {
		this.reviewTime = reviewTime;
	}

	public String getRejectionReason() {
		return rejectionReason;
	}

	public void setRejectionReason(String rejectionReason) {
		this.rejectionReason = rejectionReason;
	}

	public LocalDateTime getApplyTime() {
		return applyTime;
	}

	public void setApplyTime(LocalDateTime applyTime) {
		this.applyTime = applyTime;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

}