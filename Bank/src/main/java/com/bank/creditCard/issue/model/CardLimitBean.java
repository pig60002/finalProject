package com.bank.creditCard.issue.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "cardlimit")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardLimitBean {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "limit_id")
    private Integer limitId;

    @ManyToOne
    @JoinColumn(name = "card_id", referencedColumnName = "card_id", nullable = false)
    private com.bank.creditCard.issue.model.CardDetailBean cardDetail;

    @Column(name = "credit_limit")
    private BigDecimal creditLimit;

    @Column(name = "effective_date")
    private LocalDate effectiveDate;

    @Column(name = "status")
    private String status;
}
