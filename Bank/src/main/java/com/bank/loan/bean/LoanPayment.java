package com.bank.loan.bean;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(name = "loan_payments")
public class LoanPayment {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long paymentId;           // 還款紀錄唯一識別碼

    @Column(name = "loan_id", nullable = false)
    private String loanId;            // 貸款唯一識別碼

    @Column(name = "schedule_id")
    private Long scheduleId;          // 對應的還款排程ID（可為 null 表示非指定排程的提前還款）

    @Column(name = "payment_date", nullable = false)
    private LocalDateTime paymentDate; // 實際付款日期時間

    @Column(name = "amount_paid", nullable = false)
    private BigDecimal amountPaid;    // 實際付款金額

    @Column(name = "payment_method")
    private String paymentMethod;     // 付款方式（如轉帳、現金、信用卡）

    @Column(name = "payment_reference")
    private String paymentReference;  // 付款參考號碼（交易編號或銀行回傳編號）

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now(); // 建立時間

    // 關聯 Loans（多對一）
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loan_id", insertable = false, updatable = false)
    private Loans loan;               // 對應的貸款資料

    // 關聯 LoanRepaymentSchedule（多對一）
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id", insertable = false, updatable = false)
    private LoanRepaymentSchedule schedule; // 對應的還款排程資料

	public Long getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(Long paymentId) {
		this.paymentId = paymentId;
	}

	public String getLoanId() {
		return loanId;
	}

	public void setLoanId(String loanId) {
		this.loanId = loanId;
	}

	public Long getScheduleId() {
		return scheduleId;
	}

	public void setScheduleId(Long scheduleId) {
		this.scheduleId = scheduleId;
	}

	public LocalDateTime getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(LocalDateTime paymentDate) {
		this.paymentDate = paymentDate;
	}

	public BigDecimal getAmountPaid() {
		return amountPaid;
	}

	public void setAmountPaid(BigDecimal amountPaid) {
		this.amountPaid = amountPaid;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public String getPaymentReference() {
		return paymentReference;
	}

	public void setPaymentReference(String paymentReference) {
		this.paymentReference = paymentReference;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public Loans getLoan() {
		return loan;
	}

	public void setLoan(Loans loan) {
		this.loan = loan;
	}

	public LoanRepaymentSchedule getSchedule() {
		return schedule;
	}

	public void setSchedule(LoanRepaymentSchedule schedule) {
		this.schedule = schedule;
	}

}
