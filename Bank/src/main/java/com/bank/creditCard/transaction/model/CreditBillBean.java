package com.bank.creditCard.transaction.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.bank.creditCard.issue.model.CardDetailBean;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "creditbill")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreditBillBean {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "creditbill_id")
    private Integer creditBillId;

    // 多張帳單會對應同一張卡片
    @ManyToOne
    @JoinColumn(name = "card_id", referencedColumnName = "card_id", nullable = false)
    private CardDetailBean cardDetail;

    @Column(name = "billing_date")
    private LocalDate billingDate;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @Column(name = "total_amount", precision = 15, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "minimum_payment", precision = 15, scale = 2)
    private BigDecimal minimumPayment;

    @Column(name = "paid_amount", precision = 15, scale = 2)
    private BigDecimal paidAmount;

    @Column(name = "status", length = 20)
    private String status;
}
