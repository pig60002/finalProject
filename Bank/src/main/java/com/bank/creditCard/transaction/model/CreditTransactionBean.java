package com.bank.creditCard.transaction.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.bank.creditCard.issue.model.CardDetailBean;
import com.bank.member.bean.Member;

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
@Table(name = "credittransactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreditTransactionBean {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id")
    private Integer transactionId;

    @Column(name = "transaction_code", nullable = false)
    private Integer transactionCode;

    // 交易用的卡片
    @ManyToOne
    @JoinColumn(name = "card_id", referencedColumnName = "card_id", nullable = false)
    private CardDetailBean cardDetail;

    // 交易持卡人 (會員)
    @ManyToOne
    @JoinColumn(name = "m_id", referencedColumnName = "m_id")
    private Member member;

    // 交易所屬帳單
    @ManyToOne
    @JoinColumn(name = "bill_id")
    private CreditBillBean creditBill;

    @Column(name = "amount", precision = 15, scale = 2)
    private BigDecimal amount;

    @Column(name = "Merchant_type", length = 20)
    private String merchantType;

    @Column(name = "cashback", precision = 15, scale = 2)
    private BigDecimal cashback;

    @Column(name = "transaction_time")
    private LocalDateTime transactionTime;

    @Column(name = "description", length = 255)
    private String description;
}