package com.bank.loan.bean;

import java.math.BigDecimal;
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

@Entity
@Table(name = "credit_profiles")
public class CreditProfiles {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "profile_id")
	private int profileId; // 主鍵，自動遞增

	@Column(name = "m_id")
	private int mId; // 會員ID（外鍵）

	@Column(name = "employer_name")
	private String employerName; // 公司名稱 / 雇主名稱

	@Column(name = "occupation_type")
	private String occupationType; // 職業類型

	@Column(name = "years_of_service")
	private int yearsOfService; // 年資

	@Column(name = "monthly_income")
	private BigDecimal monthlyIncome; // 每月收入

	@Column(name = "monthly_debt")
	private BigDecimal monthlyDebt; // 每月債務支出

	@Column(name = "dti_ratio")
	private BigDecimal dtiRatio; // 負債收入比

	@Column(name = "credit_score")
	private int creditScore; // 綜合信用分數

	@Column(name = "created_at")
	private LocalDateTime createdAt; // 建立時間

	@Column(name = "updated_at")
	private LocalDateTime updatedAt; // 最後更新時間

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "m_id", insertable = false, updatable = false)
	private Member member;

	// Constructor
	public CreditProfiles() {
	}

	// Getter
	public int getProfileId() { return profileId; }
	public int getmId() { return mId; }
	public String getEmployerName() { return employerName; }
	public String getOccupationType() { return occupationType; }
	public int getYearsOfService() { return yearsOfService; }
	public BigDecimal getMonthlyIncome() { return monthlyIncome; }
	public BigDecimal getMonthlyDebt() { return monthlyDebt; }
	public BigDecimal getDtiRatio() { return dtiRatio; }
	public int getCreditScore() { return creditScore; }
	public LocalDateTime getCreatedAt() { return createdAt; }
	public LocalDateTime getUpdatedAt() { return updatedAt; }
	public Member getMember() { return member; }

	// Setter
	public void setProfileId(int profileId) { this.profileId = profileId; }
	public void setmId(int mId) { this.mId = mId; }
	public void setEmployerName(String employerName) { this.employerName = employerName; }
	public void setOccupationType(String occupationType) { this.occupationType = occupationType; }
	public void setYearsOfService(int yearsOfService) { this.yearsOfService = yearsOfService; }
	public void setMonthlyIncome(BigDecimal monthlyIncome) { this.monthlyIncome = monthlyIncome; }
	public void setMonthlyDebt(BigDecimal monthlyDebt) { this.monthlyDebt = monthlyDebt; }
	public void setDtiRatio(BigDecimal dtiRatio) { this.dtiRatio = dtiRatio; }
	public void setCreditScore(int creditScore) { this.creditScore = creditScore; }
	public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
	public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
	public void setMember(Member member) { this.member = member; }
}
