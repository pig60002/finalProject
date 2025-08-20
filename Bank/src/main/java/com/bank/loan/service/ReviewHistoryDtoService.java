package com.bank.loan.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bank.loan.bean.CreditReviewLogs;
import com.bank.loan.dao.CreditReviewLogsRepository;
import com.bank.loan.dto.ReviewHistoryDto;

/**
 * ReviewHistoryDtoService 是負責處理信用審查紀錄相關的服務類別，
 * 提供從資料庫取得審查紀錄並轉換為 ReviewHistoryDto 的功能。
 */
@Service
@Transactional
public class ReviewHistoryDtoService {
	

    // 自動注入 CreditReviewLogsRepository，用於存取信用審查紀錄資料
    @Autowired
    private CreditReviewLogsRepository crlRepo;
    
    
    // 查詢全部並轉換為ReviewHistoryDto
    public List<ReviewHistoryDto> findAll() {
    	List<CreditReviewLogs> logs = crlRepo.findAll();
    	return logs.stream()
    			.map(this::toDto)
    			.collect(Collectors.toList());
    }

    /**
     * 根據 reviewId 查詢一筆信用審查紀錄，並轉換為 ReviewHistoryDto。
     *
     * @param reviewId 要查詢的審查紀錄主鍵 ID
     * @return 對應的 ReviewHistoryDto，如果查無資料則回傳 null
     */
    public ReviewHistoryDto findById(Integer reviewId) {
        Optional<CreditReviewLogs> optional = crlRepo.findById(reviewId);

        // 如果有查到資料，轉換為 DTO 回傳；否則回傳 null
        if (optional.isPresent()) {
            return toDto(optional.get());
        } else {
            return null;
        }
    }

    /**
     * 將 CreditReviewLogs 實體轉換為 ReviewHistoryDto。
     *
     * @param review 審查紀錄實體
     * @return 對應的 DTO 物件
     */
    public ReviewHistoryDto toDto(CreditReviewLogs review) {
        // 建立 DTO 物件
        ReviewHistoryDto dto = new ReviewHistoryDto();

        // 設定審查紀錄主鍵 ID 與初步的審查人 ID（注意：下方可能會再覆蓋）
        dto.setReviewId(review.getReviewId());
        dto.setReviewerId(review.getReviewerId());

        // 如果有關聯的會員資料，設定會員名稱
        if (review.getMember() != null) {
            dto.setMName(review.getMember().getmName());
        }

        // 如果有關聯的貸款資料，設定貸款 ID
        if (review.getLoan() != null) {
            dto.setLoanId(review.getLoan().getLoanId());
        }

        // 如果有關聯的審查人（員工）物件，覆蓋設定 reviewerId（以審查人員工 ID 為主）
        if (review.getReviewer() != null) {
            dto.setReviewerId(review.getReviewer().getwId());
        }

        // 設定審查時間、信用分數、決策結果與備註
        dto.setReviewTime(review.getReviewTime());
        dto.setCreditScore(review.getCreditScore());
        dto.setDecision(review.getDecision());
        dto.setNotes(review.getNotes());

        return dto;
    }

}
