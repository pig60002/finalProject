package com.bank.loan.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bank.loan.bean.CreditReviewLogs;
import com.bank.loan.dao.CreditReviewLogsRepository;
import com.bank.loan.dto.CreditReviewDto;


@Service
@Transactional
public class CreditReviewDtoService {


	@Autowired
	private CreditReviewLogsRepository crlRepo;
	
	// 依照 reviewId 查詢轉成 DTO
    public CreditReviewDto findByIdDto(Integer reviewId) {
        Optional<CreditReviewLogs> optional = crlRepo.findById(reviewId);
        if (optional.isPresent()) {
            return toDto(optional.get());
        } else {
            return null;
        }
    }

    // 將 entity 轉為 DTO
    public CreditReviewDto toDto(CreditReviewLogs review) {
        CreditReviewDto dto = new CreditReviewDto();
        dto.setLoanId(review.getLoanId());
        dto.setReviewerId(review.getReviewerId());
        dto.setReviewedCreditScore(review.getCreditScore());
        dto.setDecision(review.getDecision());
        dto.setNotes(review.getNotes());

        // 使用 Member 關聯資料中的 mName
        if (review.getMember() != null) {
            dto.setMName(review.getMember().getmName());
        }

        return dto;
    }
	
}
