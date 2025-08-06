package com.bank.loan.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bank.loan.bean.CreditReviewLogs;
import com.bank.loan.dao.CreditReviewLogsRepository;
import com.bank.loan.dto.CreditReviewDto;

/**
 * CreditReviewDtoService 是一個服務層類別，負責處理信用審查資料的查詢與轉換。
 * 將 CreditReviewLogs 實體轉為 CreditReviewDto，方便前端或其他應用層使用。
 */
@Service
@Transactional
public class CreditReviewDtoService {

    // 自動注入 Repository，負責與資料庫操作 CreditReviewLogs 資料
    @Autowired
    private CreditReviewLogsRepository crlRepo;

    /**
     * 根據 reviewId 查詢一筆信用審查紀錄，並轉換為 DTO 格式。
     *
     * @param reviewId 要查詢的信用審查紀錄主鍵 ID
     * @return CreditReviewDto 物件，若查無資料則回傳 null
     */
    public CreditReviewDto findByIdDto(Integer reviewId) {
        Optional<CreditReviewLogs> optional = crlRepo.findById(reviewId);

        // 若有找到資料則轉為 DTO，否則回傳 null
        if (optional.isPresent()) {
            return toDto(optional.get());
        } else {
            return null;
        }
    }

    /**
     * 將 CreditReviewLogs 實體轉換為 CreditReviewDto。
     *
     * @param review 從資料庫查詢到的信用審查紀錄實體
     * @return 對應的 CreditReviewDto 資料傳輸物件
     */
    public CreditReviewDto toDto(CreditReviewLogs review) {
        // 建立一個新的 DTO 物件
        CreditReviewDto dto = new CreditReviewDto();

        // 將必要欄位轉換至 DTO
        dto.setLoanId(review.getLoanId()); // 貸款編號
        dto.setReviewerId(review.getReviewerId()); // 審查人 ID
        dto.setReviewedCreditScore(review.getCreditScore()); // 審查後的信用分數
        dto.setDecision(review.getDecision()); // 審查決策
        dto.setNotes(review.getNotes()); // 備註

        // 若有關聯的會員資料，取出 mName（會員名稱）
        if (review.getMember() != null) {
            dto.setMName(review.getMember().getmName());
        }

        // 回傳組裝完成的 DTO
        return dto;
    }
}
