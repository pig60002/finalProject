package com.bank.loan.dto;

import com.bank.loan.bean.CreditProfiles;
import com.bank.loan.bean.CreditReviewLogs;
import com.bank.loan.bean.Loans;
import com.bank.loan.enums.ApprovalStatusEnum;
import com.bank.loan.enums.LoanTermEnum;
import com.bank.loan.enums.LoanTypeEnum;
import com.bank.member.bean.Member;

public class LoanDetailDtoMapper {

    public static LoanDetailDto toDto(
        Loans loan,
        Member member,
        CreditProfiles profile,
        CreditReviewLogs crl
    ) {
        LoanDetailDto.LoanDetailDtoBuilder builder = LoanDetailDto.builder();

        // Loans 資料
        builder
            .loanId(loan.getLoanId())
            .mId(loan.getMid())
            .loanTypeId(loan.getLoanTypeId())
            .loanTermId(loan.getLoanTermId())
            .loanTerm(loan.getLoanTerm())
            .loanAmount(loan.getLoanAmount())
            .repayAccountId(loan.getRepayAccountId())
            .interestRate(loan.getInterestRate())
            .loanUpdatedAt(loan.getUpdatedAt())
            .approvalStatus(loan.getApprovalStatus())
            .createdAt(loan.getCreatedAt())
            .proofDocumentUrl(loan.getProofDocumentUrl());

        // Member 資料
        builder
            .mName(member.getmName())
            .mIdentity(member.getmIdentity())
            .mPhone(member.getmPhone())
            .mEmail(member.getmEmail())
            .mAddress(member.getmAddress());

        // CreditProfiles 資料
        if (profile != null) {
            builder
                .employerName(profile.getEmployerName())
                .occupationType(profile.getOccupationType())
                .yearsOfService(profile.getYearsOfService())
                .monthlyIncome(profile.getMonthlyIncome())
                .monthlyDebt(profile.getMonthlyDebt())
                .dtiRatio(profile.getDtiRatio())
                .baseCreditScore(profile.getCreditScore());
        }

        // CreditReviewLogs 資料
        if (crl != null) {
            builder
                .reviewerId(crl.getReviewerId())
                .reviewTime(crl.getReviewTime())
                .reviewedCreditScore(crl.getCreditScore())
                .decision(crl.getDecision())
                .notes(crl.getNotes());
        }
        
        builder
        .loanTypeName(LoanTypeEnum.fromId(loan.getLoanTypeId()))
        .loanTermName(LoanTermEnum.fromId(loan.getLoanTermId()))
        .approvalStatusName(ApprovalStatusEnum.fromCode(loan.getApprovalStatus()));
        
        return builder.build();
    }
}

