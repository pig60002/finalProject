package com.bank.loan.bean;

import java.time.LocalDateTime;

import com.bank.member.bean.Member;
import com.bank.member.bean.Worker;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "credit_review_logs")
public class CreditReviewLogs implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "review_id")
	private int reviewId; // 主鍵，自動遞增

	@Column(name = "m_id")
	private int mId; // 會員ID（外鍵）

	@Column(name = "loan_id")
	private String loanId; // 貸款ID（外鍵）

	@Column(name = "reviewer_id")
	private Integer reviewerId; // 審核人員ID（可為 NULL）

	@Column(name = "review_time")
	private LocalDateTime reviewTime; // 審核時間

	@Column(name = "credit_score")
	private Integer creditScore; // 此次審核信用分數

	@Column(name = "decision")
	private String decision; // 決策結果（通過 / 拒絕 / 補件中）

	@Column(name = "notes")
	private String notes; // 備註

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "m_id", insertable = false, updatable = false)
	private Member member;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "loan_id", insertable = false, updatable = false)
	private Loans loan;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "reviewer_id", insertable = false, updatable = false)
	private Worker reviewer;

	// Constructor
	public CreditReviewLogs() {
	}

	// Getter
	public int getReviewId() { return reviewId; }
	public int getmId() { return mId; }
	public String getLoanId() { return loanId; }
	public Integer getReviewerId() { return reviewerId; }
	public LocalDateTime getReviewTime() { return reviewTime; }
	public Integer getCreditScore() { return creditScore; }
	public String getDecision() { return decision; }
	public String getNotes() { return notes; }
	public Member getMember() { return member; }
	public Loans getLoan() { return loan; }
	public Worker getReviewer() { return reviewer; }

	// Setter
	public void setReviewId(int reviewId) { this.reviewId = reviewId; }
	public void setmId(int mId) { this.mId = mId; }
	public void setLoanId(String loanId) { this.loanId = loanId; }
	public void setReviewerId(Integer reviewerId) { this.reviewerId = reviewerId; }
	public void setReviewTime(LocalDateTime reviewTime) { this.reviewTime = reviewTime; }
	public void setCreditScore(Integer creditScore) { this.creditScore = creditScore; }
	public void setDecision(String decision) { this.decision = decision; }
	public void setNotes(String notes) { this.notes = notes; }
	public void setMember(Member member) { this.member = member; }
	public void setLoan(Loans loan) { this.loan = loan; }
	public void setReviewer(Worker reviewer) { this.reviewer = reviewer; }
}
