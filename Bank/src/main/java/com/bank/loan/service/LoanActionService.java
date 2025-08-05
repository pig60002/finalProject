package com.bank.loan.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.bank.loan.bean.Loans;
import com.bank.loan.bean.CreditReviewLogs;
import com.bank.loan.dao.LoanRepository;
import com.bank.loan.dao.CreditReviewLogsRepository;
import com.bank.loan.dto.ReviewHistoryDto;

@Service
public class LoanActionService {

    private final String uploadDir = "C:/loans/incomeProof/";

    @Autowired
    private LoanRepository lRepo;

    @Autowired
    private CreditReviewLogsRepository crRepo;
    
    public class LoanStatus {
        public static final String PENDING = "pending";         // 待審核
        public static final String SUPPLEMENT = "supplement";   // 補件中
        public static final String APPROVED = "approved";       // 審核通過
        public static final String REJECTED = "rejected";       // 拒絕
    }

    // 1. 儲存補件檔案 & 更新 loans.proofDocumentUrl
    public void uploadProof(String loanId, MultipartFile file) throws IOException {
        Loans loan = lRepo.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Loan not found"));

        String timestamp = java.time.LocalDateTime.now()
                .format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));

        // 取得原始檔名的副檔名（含點號），例如 ".pdf"
        String originalFilename = file.getOriginalFilename();
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }

        String fileName = loanId + "_" + timestamp + extension;

        Path path = Paths.get(uploadDir + fileName);
        Files.createDirectories(path.getParent());
        Files.write(path, file.getBytes());

        loan.setProofDocumentUrl(path.toString());
        // 只允許補件中才更新狀態
        if (LoanStatus.SUPPLEMENT.equals(loan.getApprovalStatus())) {
            loan.setApprovalStatus(LoanStatus.PENDING);
            loan.setUpdatedAt(LocalDateTime.now());
        }
        lRepo.save(loan);
        
     // 新增審核紀錄，記錄補件動作
        CreditReviewLogs review = new CreditReviewLogs();
        review.setLoan(loan);
        review.setLoanId(loanId);
        review.setmId(loan.getMember().getmId());
        review.setReviewerId(null);  // 這裡要傳前台登入者ID或固定ID
        review.setCreditScore(null);       // 補件不一定有分數，可設null或0
        review.setDecision("pending");
        review.setNotes("用戶上傳補件檔案");
        review.setReviewTime(LocalDateTime.now());
        crRepo.save(review);
    }
    
    
    // 2. 更新審核狀態
    public void updateStatus(String loanId, String newStatus, Integer reviewerId) {
        Loans loan = lRepo.findById(loanId)
            .orElseThrow(() -> new RuntimeException("Loan not found"));

        // 將 newStatus 轉小寫，確保大小寫一致
        String normalizedStatus = newStatus.toLowerCase();
        
        // 驗證 newStatus 是否有效
        if (!normalizedStatus.equals(LoanStatus.PENDING) &&
            !normalizedStatus.equals(LoanStatus.SUPPLEMENT) &&
            !normalizedStatus.equals(LoanStatus.APPROVED) &&
            !normalizedStatus.equals(LoanStatus.REJECTED)) {
            throw new IllegalArgumentException("Invalid loan status: " + newStatus);
        }
        
        loan.setApprovalStatus(normalizedStatus);
        loan.setUpdatedAt(LocalDateTime.now());
        lRepo.save(loan);

        CreditReviewLogs log = new CreditReviewLogs();
        log.setLoanId(loanId);
        log.setLoan(loan);
        log.setmId(loan.getMember().getmId());
        log.setReviewerId(reviewerId);
        log.setCreditScore(null);
        log.setDecision(newStatus);
        log.setNotes("狀態由審核人員變更為 " + newStatus);
        log.setReviewTime(LocalDateTime.now());
        crRepo.save(log);
    }

    

    // 3. 儲存一筆審核紀錄
    public void saveReview(String loanId, ReviewHistoryDto dto) {
        Loans loan = lRepo.findById(loanId).orElseThrow(() -> new RuntimeException("Loan not found"));

        CreditReviewLogs review = new CreditReviewLogs();
        review.setLoan(loan);
        review.setLoanId(loanId);
        review.setmId(loan.getMember().getmId());
        review.setReviewerId(dto.getReviewerId());
        review.setCreditScore(dto.getCreditScore());
        review.setDecision(dto.getDecision());
        review.setNotes(dto.getNotes());
        review.setReviewTime(java.time.LocalDateTime.now());

        crRepo.save(review);

        // 若有需要更新 loan.approvalStatus
        if (dto.getDecision() != null) {
            loan.setApprovalStatus(dto.getDecision());
            loan.setUpdatedAt(LocalDateTime.now());
            lRepo.save(loan);
        }
    }
}
