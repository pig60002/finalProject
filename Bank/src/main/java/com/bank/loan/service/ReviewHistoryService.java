package com.bank.loan.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bank.loan.bean.CreditReviewLogs;
import com.bank.loan.bean.Loans;
import com.bank.loan.dao.CreditReviewLogsRepository;
import com.bank.loan.dao.LoanRepository;
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
    
    @Autowired
    private LoanEmailService eService;
    
    @Autowired
    private LoanRepository loanRepo;

    /**
     * 儲存或更新審查紀錄資料。
     * 若 DTO 中 reviewId 為非 0，則視為更新，否則為新增。
     * 
     * @param dto 從前端或其他來源接收到的 DTO 物件
     * @return 儲存後的 CreditReviewLogs 實體物件
     */
    public CreditReviewLogs saveOrUpdateFromDto(ReviewHistoryDto dto) {
        CreditReviewLogs entity;

        String oldDecision = null;

        // 嘗試抓舊資料
        if (dto.getReviewId() != 0) {
            Optional<CreditReviewLogs> optional = crlRepo.findById(dto.getReviewId());
            entity = optional.orElseGet(CreditReviewLogs::new);
            oldDecision = entity.getDecision();
        } else {
            entity = new CreditReviewLogs();
        }

        // 設定欄位
        entity.setReviewerId(dto.getReviewerId());
        entity.setReviewTime(dto.getReviewTime());
        entity.setCreditScore(dto.getCreditScore());
        entity.setDecision(dto.getDecision());
        entity.setNotes(dto.getNotes());
        entity.setLoanId(dto.getLoanId());

        // 🔹 自動抓 Member
        if (dto.getLoanId() != null) {
            Optional<Loans> loanOpt = loanRepo.findById(dto.getLoanId());
            if (loanOpt.isPresent()) {
                Loans loan = loanOpt.get();
                Integer memberId = loan.getMember().getmId();
                entity.setmId(memberId);

                // 直接用 loan.getMember() 寄信
                if (oldDecision == null || !oldDecision.equals(dto.getDecision())) {
                    String email = loan.getMember().getmEmail();
                    String name = loan.getMember().getmName();
                    eService.sendReviewDecisionEmail(email, name, dto.getDecision(), dto.getNotes());
                }
            }
        }


        // 儲存
        CreditReviewLogs saved = crlRepo.save(entity);

        // decision 變更才寄信
        if (saved.getMember() != null && dto.getDecision() != null) {
            if (oldDecision == null || !oldDecision.equals(dto.getDecision())) {
                String email = saved.getMember().getmEmail();
                String name = saved.getMember().getmName();
                eService.sendReviewDecisionEmail(email, name, dto.getDecision(), dto.getNotes());
            }
        }
        System.out.println("saveOrUpdateFromDto called. Old decision: " + oldDecision + ", new decision: " + dto.getDecision());

        return saved;
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
                dto.setMEmail(log.getMember().getmEmail());
            }

            return dto;
        } else {
            return null;
        }
    }

}
