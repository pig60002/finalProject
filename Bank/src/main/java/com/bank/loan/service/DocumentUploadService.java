package com.bank.loan.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.bank.loan.bean.Loans;
import com.bank.loan.dao.LoanRepository;

/**
 * DocumentUploadService 提供檔案上傳並更新貸款紀錄中文件路徑的服務。
 */
@Service
@Transactional
public class DocumentUploadService {

    // 自動注入 LoanRepository，用於資料庫操作
    @Autowired
    private LoanRepository loanRepo;

    // 預設上傳目錄，應改為實際存在的伺服器目錄
    private final String uploadDir = "/path/to/upload/folder";

    /**
     * 儲存貸款相關的證明文件，並更新資料庫中的 URL 欄位。
     *
     * @param loanId 貸款編號
     * @param file 上傳的檔案
     * @return 上傳後的文件 URL（可用於前端存取）
     * @throws IOException 若檔案儲存過程中發生錯誤
     */
    public String saveProofDocument(String loanId, MultipartFile file) throws IOException {
        // 1. 檢查是否有檔案上傳
        if (file.isEmpty()) {
            throw new IOException("File is empty"); // 檔案為空時丟出例外
        }

        // 2. 取得原始檔名並建立新檔名（加上 loanId 與時間戳記，避免檔名重複）
        String originalFilename = file.getOriginalFilename();
        String newFileName = loanId + "_" + System.currentTimeMillis() + "_" + originalFilename;

        // 建立儲存路徑（uploadDir 是預設的上傳目錄）
        Path targetPath = Paths.get(uploadDir).resolve(newFileName);

        // 3. 建立上傳資料夾（若不存在）
        Files.createDirectories(targetPath.getParent());

        // 4. 儲存檔案到指定路徑
        file.transferTo(targetPath.toFile());

        // 5. 查詢該筆貸款紀錄並更新其 proofDocumentUrl 欄位
        Loans loan = loanRepo.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Loan not found: " + loanId));

        // 儲存檔案的相對路徑（或可改為完整 URL）
        loan.setProofDocumentUrl("/uploads/" + newFileName);

        // 將更新後的資料儲存回資料庫
        loanRepo.save(loan);

        // 6. 回傳上傳後的文件 URL，供前端顯示或下載
        return "/uploads/" + newFileName;
    }
}
