package com.bank.creditCard.dto;

import java.time.LocalDateTime;


import com.bank.member.bean.Member;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.bank.creditCard.application.model.CardApplicationBean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CardApplicationDTO {
	private Integer applicationId;          
    private Integer userId;      
    private Integer cardType;    
    private String idPhotoFront;
    private String idPhotoBack;
    private String financialProof;
    @JsonFormat(pattern ="yyyy-MM-dd")
    private LocalDateTime applyDate;
    @JsonFormat(pattern ="yyyy-MM-dd")
    private LocalDateTime reviewDate;
    private String status;
    private MemberDto member;
    @JsonFormat(pattern ="yyyy-MM-dd")
    private String reviewComment;
}
