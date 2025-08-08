package com.bank.creditCard.issue.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.bank.creditCard.cardType.model.CardTypeBean;
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
@Table(name = "card_detail")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardDetailBean {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "card_id")
    private Integer cardId;

    @Column(name = "card_code", nullable = false, unique = true)
    private String cardCode; // 卡號

    @Column(name = "cvv_code", nullable = false)
    private String cvvCode;

    @Column(name = "expiration_date", nullable = false)
    private LocalDate expirationDate;

    @Column(name = "credit_limit", nullable = false)
    private BigDecimal creditLimit;

    @Column(name = "currentBalance", nullable = false)
    private BigDecimal currentBalance;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "issued_date", nullable = false)
    private LocalDate issuedDate;

    // 關聯欄位 - 多對一
    @ManyToOne
    @JoinColumn(name = "m_id", referencedColumnName = "m_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "card_type_id", referencedColumnName = "card_type_id")
    private CardTypeBean cardType;
    
    public static final String STATUS_ACTIVE = "ACTIVE";
    public static final String STATUS_INACTIVE = "INACTIVE";
    public static final String STATUS_SUSPEND = "SUSPEND";

}
