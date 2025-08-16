package com.bank.loan.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bank.loan.bean.CreditReviewLogs;
import com.bank.loan.dao.CreditReviewLogsRepository;
import com.bank.loan.dto.ReviewHistoryDto;

/**
 * ReviewHistoryService 提供處理信用審查紀錄（CreditReviewLogs）資料的服務，
 * 包含新增、修改與查詢功能，並透過 DTO（ReviewHistoryDto）來進行資料轉換。
 */
@Service
@Transactional
public class ReviewHistoryService {

    // 注入 Repository 來與資料庫操作 CreditReviewLogs 資料
    @Autowired
    private CreditReviewLogsRepository crlRepo;

    /**
     * 儲存或更新審查紀錄資料。
     * 若 DTO 中 reviewId 為非 0，則視為更新，否則為新增。
     * 
     * @param dto 從前端或其他來源接收到的 DTO 物件
     * @return 儲存後的 CreditReviewLogs 實體物件
     */
    public CreditReviewLogs saveOrUpdateFromDto(ReviewHistoryDto dto) {
        CreditReviewLogs entity;

        // 如果 reviewId 不為 0，嘗試從資料庫取得既有資料（更新模式）
        if (dto.getReviewId() != 0) {
            Optional<CreditReviewLogs> optional = crlRepo.findById(dto.getReviewId());
            entity = optional.orElseGet(CreditReviewLogs::new); // 若找不到則新建
        } else {
            // 若 reviewId 為 0，表示新增一筆新資料
            entity = new CreditReviewLogs();
        }

        // 將 DTO 中的欄位對應到實體中
        entity.setReviewerId(dto.getReviewerId());
        entity.setReviewTime(dto.getReviewTime());
        entity.setCreditScore(dto.getCreditScore());
        entity.setDecision(dto.getDecision());
        entity.setNotes(dto.getNotes());

        // 🔶 注意事項：
        // loanId、member 等關聯欄位尚未處理，
        // 若有需要設置 Loan 或 Member 物件，應額外查詢並設入 entity 中

        // 儲存資料至資料庫（新增或更新）
        return crlRepo.save(entity);
    }

    /**
     * 根據 reviewId 查詢信用審查紀錄，並轉換為 DTO。
     * 
     * @param reviewId 要查詢的主鍵 ID
     * @return 對應的 DTO，若查無資料則回傳 null
     */
    public ReviewHistoryDto findById(Integer reviewId) {
        Optional<CreditReviewLogs> optional = crlRepo.findById(reviewId);

        if (optional.isPresent()) {
            CreditReviewLogs log = optional.get();

            // 將 entity 內容轉換為 DTO 回傳
            ReviewHistoryDto dto = new ReviewHistoryDto();
            dto.setReviewId(log.getReviewId());
            dto.setReviewTime(log.getReviewTime());
            dto.setCreditScore(log.getCreditScore());
            dto.setDecision(log.getDecision());
            dto.setNotes(log.getNotes());
            dto.setReviewerId(log.getReviewerId());
            dto.setLoanId(log.getLoanId()); // 假設 loanId 是直接存在於 CreditReviewLogs 中

            // 若有關聯的 Member 物件，取出名稱設入 DTO
            if (log.getMember() != null) {
                dto.setMName(log.getMember().getmName());
            }

            return dto;
        } else {
            return null;
        }
    }

}
