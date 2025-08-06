package com.bank.loan.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bank.loan.bean.CreditReviewLogs;
import com.bank.loan.dao.CreditReviewLogsRepository;
import com.bank.loan.dto.DocumentManagementDto;

/**
 * DocumentManagementDtoService 負責處理與信用審查紀錄（CreditReviewLogs）相關的服務邏輯，
 * 並將其轉換為用於前端顯示或 API 傳輸的 DTO（DocumentManagementDto）格式。
 */
@Service
@Transactional
public class DocumentManagementDtoService {

    // 自動注入 CreditReviewLogsRepository，用於操作資料庫中的 CreditReviewLogs 實體
    @Autowired
    private CreditReviewLogsRepository crlRepo;

    /**
     * 根據 reviewId 查詢信用審查紀錄，並轉換為 DocumentManagementDto。
     * 
     * @param reviewId 要查詢的審查紀錄 ID
     * @return 如果找到紀錄，則回傳對應的 DTO；否則回傳 null
     */
    public DocumentManagementDto findByIdDto(Integer reviewId) {
        // 使用 Optional 防止 NullPointerException
        Optional<CreditReviewLogs> optional = crlRepo.findById(reviewId);

        // 如果找到紀錄則轉換為 DTO；否則回傳 null
        if (optional.isPresent()) {
            return toDto(optional.get());
        } else {
            return null;
        }
    }
    
    public DocumentManagementDto findLatestByLoanId(String loanId) {
        // 用 repo 查詢最新的 CreditReviewLogs (假設有時間欄位 reviewTime)
        Optional<CreditReviewLogs> optional = crlRepo.findTopByLoanIdOrderByReviewTimeDesc(loanId);

        if (optional.isPresent()) {
            return toDto(optional.get());
        } else {
            return null;
        }
    }


    /**
     * 將 CreditReviewLogs 實體轉換為 DocumentManagementDto。
     * 
     * @param review 要轉換的信用審查紀錄實體
     * @return 對應的 DTO
     */
    public DocumentManagementDto toDto(CreditReviewLogs review) {
        // 建立一個新的 DTO 物件
        DocumentManagementDto dto = new DocumentManagementDto();

        // 設定貸款 ID
        dto.setLoanId(review.getLoanId());

        // 若關聯的貸款資料存在，則取得其文件上傳網址
        if (review.getLoan() != null) {
            dto.setProofDocumentUrl(review.getLoan().getProofDocumentUrl());
        }

        // 設定審查時間、決策結果與備註
        dto.setReviewTime(review.getReviewTime());
        dto.setDecision(review.getDecision());
        dto.setNotes(review.getNotes());

        // 若關聯的會員資料存在，則取得會員名稱
        if (review.getMember() != null) {
            dto.setMName(review.getMember().getmName());
        }

        // 回傳組裝完成的 DTO
        return dto;
    }

}
