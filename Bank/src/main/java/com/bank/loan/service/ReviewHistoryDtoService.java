package com.bank.loan.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bank.loan.bean.CreditReviewLogs;
import com.bank.loan.dao.CreditReviewLogsRepository;
import com.bank.loan.dto.ReviewHistoryDto;

@Service
@Transactional
public class ReviewHistoryDtoService {
	
	@Autowired
	private CreditReviewLogsRepository crlRepo;

	public ReviewHistoryDto findById(Integer reviewId) {
		Optional<CreditReviewLogs> optional = crlRepo.findById(reviewId);
		if(optional.isPresent()) {
			return toDto(optional.get());
		} else {
			return null;
		}
	}
	
	public ReviewHistoryDto toDto(CreditReviewLogs review) {
		ReviewHistoryDto dto = new ReviewHistoryDto();
		dto.setReviewId(review.getReviewId());
		dto.setReviewerId(review.getReviewerId());

		if (review.getMember() != null) {
            dto.setMName(review.getMember().getmName());
        }
		
		if (review.getLoan() != null) {
            dto.setLoanId(review.getLoan().getLoanId());
        }
		
		if (review.getReviewer() != null) {
            dto.setReviewerId(review.getReviewer().getWId());
        }
		
		dto.setReviewTime(review.getReviewTime());
		dto.setCreditScore(review.getCreditScore());
		dto.setDecision(review.getDecision());
		dto.setNotes(review.getNotes());
		
		return dto;
	}
	
}
