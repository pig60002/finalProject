package com.bank.loan.service;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.bank.account.bean.Account;
import com.bank.account.dao.AccountRepository;
import com.bank.loan.bean.LoanTerm;
import com.bank.loan.bean.LoanType;
import com.bank.loan.bean.Loans;
import com.bank.loan.dao.LoanRepository;
import com.bank.loan.dao.LoanTermRepository;
import com.bank.loan.dao.LoanTypeRepository;
import com.bank.loan.dto.LoanCreateDto;

@Service
@Transactional
public class LoanService {

	@Autowired
	private LoanRepository loanRepo;

	@Autowired
	private LoanTypeRepository loanTypeRepo;

	@Autowired
	private LoanTermRepository loanTermRepo;
	
	@Autowired
	private AccountRepository accountRepo;

	public List<Account> getAccountsByMemberId(Integer mId) {
	    return accountRepo.findByMId(mId);
	}

	/**
	 * 根據貸款類型與期數生成結構化的 LoanId。 格式範例: 72 + loanTypeCode + termTypeCode + 5碼流水號 +
	 * 1碼隨機數
	 */
	private String generateStructuredLoanId(String loanTypeId, int loanTerm) {
		String bankCode = "7"; // 固定銀行代碼
		String accountType = "2"; // 貸款代號
		String loanTypeCode;
		String termTypeCode;

		// 轉換 loanTypeId 為代碼
		switch (loanTypeId) {
		case "LT001":
			loanTypeCode = "1";
			break;
		case "LT002":
			loanTypeCode = "2";
			break;
		case "LT003":
			loanTypeCode = "3";
			break;
		default:
			loanTypeCode = "9";
			break; // 其他類型
		}

		// 依期數分類碼
		if (loanTerm <= 12) {
			termTypeCode = "1"; // 短期
		} else if (loanTerm <= 36) {
			termTypeCode = "2"; // 中期
		} else {
			termTypeCode = "3"; // 長期
		}

		// 前綴組合
		String prefix = bankCode + accountType + loanTypeCode + termTypeCode;

		// 查詢現有最大流水號（從資料庫）
		String maxSerial = loanRepo.findMaxSerialNo();
		int serial = (maxSerial == null) ? 1 : Integer.parseInt(maxSerial) + 1;
		String serialFormatted = String.format("%05d", serial);

		// 隨機一碼
		int randomDigit = (int) (Math.random() * 10);

		return prefix + serialFormatted + randomDigit;
	}

	/**
	 * 建立新的貸款申請（含檔案處理）
	 */
	public Loans createLoan(LoanCreateDto dto, MultipartFile file) throws IOException {
		// === 檔案處理 ===
		if (file == null || file.isEmpty()) {
			throw new IllegalArgumentException("上傳的檔案為空");
		}
		
		// === 驗證帳戶使否屬於該會員 ===
		Account selectedAccount = accountRepo.findByAccountId(dto.getRepayAccountId());
		if (selectedAccount == null || !selectedAccount.getmId().equals(dto.getMId())) {
		    throw new IllegalArgumentException("所選帳戶不存在或不屬於該會員");
		}

		// 設定資料夾與儲存路徑
		String baseUploadDir = "C:/loans/incomeProof/";
		String uploadPath = baseUploadDir; // 直接存放在 C:/loans/incomeProof/
		File dir = new File(uploadPath);
		if (!dir.exists()) {
		    dir.mkdirs();
		}
		
		// 取得檔案副檔名
        String originalFilename = file.getOriginalFilename();
        String fileExtension = "";
        int dotIndex = originalFilename.lastIndexOf('.');
        if (dotIndex >= 0) {
            fileExtension = originalFilename.substring(dotIndex);
        }


		// 以 loanId + 建立時間 命名檔案
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String loanId = generateStructuredLoanId(dto.getLoanTypeId(), dto.getLoanTerm());
        String fileName = loanId + "_" + timestamp + fileExtension;

        File dest = new File(dir, fileName);
        file.transferTo(dest);

        // 設定 DTO 中的檔案相對路徑（相對於你的 uploads 目錄）
        dto.setIncomeProofPath("uploads/" + fileName);

		// === 取得貸款類型與期數資訊（含利率） ===
		LoanType loanType = loanTypeRepo.findById(dto.getLoanTypeId())
				.orElseThrow(() -> new IllegalArgumentException("找不到指定的貸款類型"));

		LoanTerm loanTerm = loanTermRepo.findById(dto.getLoanTermId())
				.orElseThrow(() -> new IllegalArgumentException("找不到指定的貸款期數"));

		BigDecimal baseRate = loanType.getBaseInterestRate(); // 基準利率
		BigDecimal adjustmentRate = loanTerm.getTermAdjustmentRate(); // 期數加成利率

		// === 建立 Loans 實體並設定欄位 ===
		Loans loan = new Loans();
		loan.setLoanId(generateStructuredLoanId(dto.getLoanTypeId(), dto.getLoanTerm()));
		loan.setLoanTypeId(dto.getLoanTypeId());
		loan.setLoanTermId(dto.getLoanTermId());
		loan.setLoanTerm(dto.getLoanTerm());
		loan.setMid(dto.getMId());
		loan.setLoanAmount(dto.getLoanAmount());
		loan.setLoanstartDate(LocalDate.now());
		loan.setApprovalStatus("pending");
		loan.setInterestRate(baseRate.add(adjustmentRate)); // 合併總利率
		loan.setRepayAccountId(dto.getRepayAccountId());
		loan.setProofDocumentUrl(dto.getIncomeProofPath());

		LocalDateTime now = LocalDateTime.now();
		loan.setCreatedAt(now);
		loan.setUpdatedAt(now);

		// === 儲存至資料庫 ===
		return loanRepo.save(loan);
	}
}
