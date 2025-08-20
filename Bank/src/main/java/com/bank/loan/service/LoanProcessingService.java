package com.bank.loan.service;

import java.io.IOException;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.bank.loan.bean.Loans;
import com.bank.loan.bean.CreditReviewLogs;
import com.bank.loan.dao.LoanRepository;
import com.bank.loan.dao.CreditReviewLogsRepository;
import com.bank.loan.dto.ReviewHistoryDto;
import com.bank.loan.util.FileUploadUtil;

import jakarta.transaction.Transactional;

/**
 * LoanProcessingService 處理與貸款審核流程相關的邏輯，
 * 包括上傳補件文件、變更審核狀態與儲存審核紀錄等。
 */
@Service
@Transactional
public class LoanProcessingService {

    @Autowired
    private LoanRepository lRepo;

    @Autowired
    private CreditReviewLogsRepository crRepo;
    
    /**
     * 定義貸款狀態的常數類別，避免硬編碼。
     */
    public class LoanStatus {
        public static final String PENDING = "pending";         // 待審核
        public static final String SUPPLEMENT = "supplement";   // 補件中
        public static final String APPROVED = "approved";       // 審核通過
        public static final String REJECTED = "rejected";       // 審核拒絕
    }

    /**
     * 1. 儲存補件文件，並更新 Loans 表中的 proofDocumentUrl 與狀態。
     *
     * @param loanId 貸款編號
     * @param file 使用者上傳的檔案
     * @throws IOException 若檔案儲存失敗
     */
    public void uploadProof(String loanId, MultipartFile file) throws IOException {
    	Loans loan = lRepo.findById(loanId).orElseThrow(() -> new RuntimeException("Loan not found"));

    	String relativePath = FileUploadUtil.saveFile(loanId, file, "loanImg");

        loan.setProofDocumentUrl(relativePath);

        if (LoanStatus.SUPPLEMENT.equals(loan.getApprovalStatus())) {
            loan.setApprovalStatus(LoanStatus.PENDING);
            loan.setUpdatedAt(LocalDateTime.now());
        }

        lRepo.save(loan);

        // 新增一筆補件的審核紀錄
        CreditReviewLogs review = new CreditReviewLogs();
        review.setLoan(loan);
        review.setLoanId(loanId);
        review.setmId(loan.getMember().getmId());
        review.setReviewerId(null);  // 補件動作無審核人，可設 null 或固定值
        review.setCreditScore(null); // 補件時通常無信用分數
        review.setDecision("pending");
        review.setNotes("用戶上傳補件檔案");
        review.setReviewTime(LocalDateTime.now());

        crRepo.save(review);
    }

    /**
     * 2. 更新貸款的審核狀態，並記錄此變更至 CreditReviewLogs。
     *
     * @param loanId      貸款編號
     * @param newStatus   新狀態（需為合法狀態：pending, supplement, approved, rejected）
     * @param reviewerId  審核人員 ID
     * @param notes       備註
     */
    public void updateStatus(String loanId, String newStatus, Integer reviewerId, String notes) {
        // 1. 取得貸款資料
        Loans loan = lRepo.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Loan not found"));

        // 2. 統一狀態小寫處理
        String normalizedStatus = newStatus.toLowerCase();

        // 3. 驗證是否為合法狀態
        if (!normalizedStatus.equals(LoanStatus.PENDING) &&
            !normalizedStatus.equals(LoanStatus.SUPPLEMENT) &&
            !normalizedStatus.equals(LoanStatus.APPROVED) &&
            !normalizedStatus.equals(LoanStatus.REJECTED)) {
            throw new IllegalArgumentException("Invalid loan status: " + newStatus);
        }

        // 4. 更新貸款狀態與修改時間
        LocalDateTime now = LocalDateTime.now();
        loan.setApprovalStatus(normalizedStatus);
        loan.setUpdatedAt(now);
        lRepo.saveAndFlush(loan);

        // 5. 記錄審核變更歷程
        CreditReviewLogs log = new CreditReviewLogs();
        log.setLoanId(loanId);
        log.setLoan(loan);
        log.setmId(loan.getMember().getmId());
        log.setReviewerId(reviewerId);
        log.setCreditScore(null); // 改變狀態不一定會影響信用分數
        log.setDecision(normalizedStatus);
        log.setNotes(notes);
        log.setReviewTime(now);

        // 6. 儲存審核紀錄
        crRepo.save(log);
    }



    /**
     * 3. 儲存一筆完整的審核紀錄，包含分數與決策結果。
     *
     * @param loanId 貸款編號
     * @param dto 前端傳入的審核紀錄資料（DTO）
     */
    public void saveReview(String loanId, ReviewHistoryDto dto) {
        Loans loan = lRepo.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Loan not found"));

        // 組裝審核紀錄
        CreditReviewLogs review = new CreditReviewLogs();
        review.setLoan(loan);
        review.setLoanId(loanId);
        review.setmId(loan.getMember().getmId());
        review.setReviewerId(dto.getReviewerId());
        review.setCreditScore(dto.getCreditScore());
        review.setDecision(dto.getDecision());
        review.setNotes(dto.getNotes());
        review.setReviewTime(LocalDateTime.now());

        crRepo.save(review);

        // 若決策結果有設定，也更新主貸款狀態
        if (dto.getDecision() != null) {
            loan.setApprovalStatus(dto.getDecision());
            loan.setUpdatedAt(LocalDateTime.now());
            lRepo.save(loan);
        }
    }
}
