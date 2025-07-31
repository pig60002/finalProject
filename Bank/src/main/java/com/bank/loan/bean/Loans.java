package com.bank.loan.bean;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.bank.member.bean.Member;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;


@Entity
@Table(name = "loans")
public class Loans {
	
	@Id
    @Column(name = "loan_id")
	private String loanId;                // 貸款唯一識別碼
	@Column(name = "m_id")
	private int mId;                      // 顧客 ID（對應 member 的主鍵）
	@Column(name = "loan_type_id")
	private String loanTypeId;           // 貸款類型 ID
	@Column(name = "loan_term_id")
	private String loanTermId;           // 期數分類 ID
	@Column(name = "loan_term")
	private int loanTerm;                // 貸款期數（通常以月為單位）
	@Column(name = "loan_amount")
	private BigDecimal loanAmount;      // 貸款金額
	@Column(name = "interest_rate")
	private BigDecimal interestRate;    // 實際利率
	@Column(name = "loanstart_date")
	private LocalDate loanstartDate;    // 貸款開始日期
	@Column(name = "approval_status")
	private String approvalStatus;      // 審核狀態
	@Column(name = "created_at")
	private LocalDateTime createdAt;    // 建立時間
	@Column(name = "updated_at")
	private LocalDateTime updatedAt;    // 更新時間
	@Column(name = "proof_document_url")
	private String proofDocumentUrl;    // 圖片存取位置
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "m_id", insertable = false, updatable = false)
	private Member member;
    
	@ManyToOne
	@JoinColumn(name = "loan_type_id", insertable = false, updatable = false)
	private LoanType loanTypeInfo;

	@ManyToOne
	@JoinColumn(name = "loan_term_id", insertable = false, updatable = false)
	private LoanTerm loanTermInfo;

    
	public Loans() {
	}
	
	// getting
	public String getLoanId() { return loanId; }
	public int getMid() { return mId; }
	public String getLoanTypeId() { return loanTypeId; }
	public String getLoanTermId() { return loanTermId; }
	public int getLoanTerm() { return loanTerm; }
	public BigDecimal  getLoanAmount() { return loanAmount; }
	public BigDecimal  getInterestRate() { return interestRate; }
	public LocalDate getLoanstartDate() { return loanstartDate; }
	public String getApprovalStatus() { return approvalStatus; }
	public LocalDateTime getCreatedAt() { return createdAt; }
	public LocalDateTime getUpdatedAt() { return updatedAt; }
	public Member getMember() { return member; }
	public String getProofDocumentUrl() { return proofDocumentUrl; }


	// setting
	public void setLoanId(String loanId) { this.loanId = loanId; }
	public void setMid(int mId) { this.mId = mId; }
	public void setLoanTypeId(String loanTypeId) { this.loanTypeId = loanTypeId; }
	public void setLoanTermId(String loanTermId) { this.loanTermId = loanTermId; }
	public void setLoanTerm(int loanTerm) { this.loanTerm = loanTerm; }
	public void setLoanAmount(BigDecimal loanAmount) { this.loanAmount = loanAmount; }
	public void setInterestRate(BigDecimal interestRate) { this.interestRate = interestRate; }
	public void setLoanstartDate(LocalDate loanstartDate) { this.loanstartDate = loanstartDate; }
	public void setApprovalStatus(String approvalStatus) { this.approvalStatus = approvalStatus; }
	public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
	public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
	public void setMember(Member member) { this.member = member; }
	public void setProofDocumentUrl(String proofDocumentUrl) { this.proofDocumentUrl = proofDocumentUrl; }
}
