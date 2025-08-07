package com.bank.loan.service;

import java.io.IOException;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.bank.loan.bean.Loans;
import com.bank.loan.dao.LoanRepository;
import com.bank.loan.util.FileUploadUtil;

/**
 * DocumentUploadService 提供檔案上傳並更新貸款紀錄中文件路徑的服務。
 */
@Service
@Transactional
public class DocumentUploadService {

    // 自動注入 LoanRepository，用於資料庫操作
    @Autowired
    private LoanRepository loanRepo;

    /**
     * 儲存貸款相關的證明文件，並更新資料庫中的 URL 欄位。
     *
     * @param loanId 貸款編號
     * @param file 上傳的檔案
     * @return 上傳後的文件 URL（可用於前端存取）
     * @throws IOException 若檔案儲存過程中發生錯誤
     */
    public String saveProofDocument(String loanId, MultipartFile file) throws IOException {
    	String relativePath = FileUploadUtil.saveFile(loanId, file);

        Loans loan = loanRepo.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Loan not found: " + loanId));

        loan.setProofDocumentUrl(relativePath);
        loanRepo.save(loan);

        System.out.println("Uploaded to: " + Paths.get(FileUploadUtil.UPLOAD_DIR).resolve(relativePath).toAbsolutePath());
        return relativePath;
    } 
}
