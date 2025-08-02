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

@Service
@Transactional
public class DocumentManagementService {

    @Autowired
    private LoanRepository loanRepo;

    // 預設上傳目錄 
    private final String uploadDir = "/path/to/upload/folder";

    public String saveProofDocument(String loanId, MultipartFile file) throws IOException {
        // 1. 先檢查檔案是否存在
        if (file.isEmpty()) {
            throw new IOException("File is empty");
        }

        // 2. 取得檔名並決定存放路徑
        String originalFilename = file.getOriginalFilename();
        String newFileName = loanId + "_" + System.currentTimeMillis() + "_" + originalFilename;
        Path targetPath = Paths.get(uploadDir).resolve(newFileName);

        // 3. 儲存檔案到指定路徑
        Files.createDirectories(targetPath.getParent());
        file.transferTo(targetPath.toFile());

        // 4. 更新資料庫貸款紀錄的 proofDocumentUrl 欄位（存成相對路徑或完整URL）
        Loans loan = loanRepo.findById(loanId)
                        .orElseThrow(() -> new RuntimeException("Loan not found: " + loanId));
        loan.setProofDocumentUrl("/uploads/" + newFileName);
        loanRepo.save(loan);

        // 5. 回傳檔案路徑或URL
        return "/uploads/" + newFileName;
    }
}

