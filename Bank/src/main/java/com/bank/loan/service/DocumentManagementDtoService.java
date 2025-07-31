package com.bank.loan.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bank.loan.bean.CreditReviewLogs;
import com.bank.loan.dao.CreditReviewLogsRepository;
import com.bank.loan.dto.DocumentManagementDto;

@Service
@Transactional
public class DocumentManagementDtoService {
	
	@Autowired
	private CreditReviewLogsRepository crlRepo;
	
	public DocumentManagementDto finByIdDto(Integer reviewId) {
		Optional<CreditReviewLogs> optional = crlRepo.findById(reviewId);
        if (optional.isPresent()) {
            return toDto(optional.get());
        } else {
            return null;
        }
	}
	
	public DocumentManagementDto toDto(CreditReviewLogs review) {
		DocumentManagementDto dto = new DocumentManagementDto();
		dto.setLoanId(review.getLoanId());
		
		if(review.getLoan() != null) {
			dto.setProofDocumentUrl(review.getLoan().getProofDocumentUrl());
		}
		
		dto.setReviewTime(review.getReviewTime());
		dto.setDecision(review.getDecision());
		dto.setNotes(review.getNotes());
		
		if(review.getMember() != null) {
			dto.setMName(review.getMember().getmName());
		}
		
		return dto;
		
	}

}
