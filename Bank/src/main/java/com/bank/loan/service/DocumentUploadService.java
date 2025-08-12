package com.bank.loan.service;

import java.io.IOException;

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

	@Autowired
    private LoanRepository loanRepo;

	// 已有的證明文件上傳
    public String saveProofDocument(String loanId, MultipartFile file) throws IOException {
        String relativePath = FileUploadUtil.saveFile(loanId, file, "uploadImg/loanImg");

        Loans loan = loanRepo.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Loan not found: " + loanId));

        loan.setProofDocumentUrl(relativePath);
        loanRepo.save(loan);

        return relativePath;
    }

    // 新增的合約檔案上傳
    public String saveContractDocument(String loanId, MultipartFile file) throws IOException {
        String relativePath = FileUploadUtil.saveFile(loanId, file, "uploadImg/contract");

        Loans loan = loanRepo.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Loan not found: " + loanId));

        loan.setContractPath(relativePath);
        loanRepo.save(loan);

        return relativePath;
    }
}
