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
public class ReviewHistoryService {
	
	@Autowired
	private CreditReviewLogsRepository crlRepo;
	
	// 儲存/更新資料
	public CreditReviewLogs saveOrUpdateFromDto(ReviewHistoryDto dto) {
	    CreditReviewLogs entity;

	    if (dto.getReviewId() != 0) {
	        Optional<CreditReviewLogs> optional = crlRepo.findById(dto.getReviewId());
	        entity = optional.orElseGet(CreditReviewLogs::new);
	    } else {
	        entity = new CreditReviewLogs();
	    }

	    // 映射欄位（部分欄位需根據你的實際 CreditReviewLogs Entity 結構來設定）
	    entity.setReviewerId(dto.getReviewerId());
	    entity.setReviewTime(dto.getReviewTime());
	    entity.setCreditScore(dto.getCreditScore());
	    entity.setDecision(dto.getDecision());
	    entity.setNotes(dto.getNotes());

	    // 建議另外處理 loanId, mName，如果是需要關聯 member 或 loan entity 的話要查出來再設定

	    return crlRepo.save(entity);
	}
	
	// 查詢資料轉成 DTO
	public ReviewHistoryDto findById(Integer reviewId) {
	    Optional<CreditReviewLogs> optional = crlRepo.findById(reviewId);

	    if (optional.isPresent()) {
	        CreditReviewLogs log = optional.get();

	        ReviewHistoryDto dto = new ReviewHistoryDto();
	        dto.setReviewId(log.getReviewId());
	        dto.setReviewTime(log.getReviewTime());
	        dto.setCreditScore(log.getCreditScore());
	        dto.setDecision(log.getDecision());
	        dto.setNotes(log.getNotes());
	        dto.setReviewerId(log.getReviewerId());
	        dto.setLoanId(log.getLoanId());

	        if (log.getMember() != null) {
	            dto.setMName(log.getMember().getmName());
	        }

	        return dto;
	    } else {
	        return null;
	    }
	}


}
