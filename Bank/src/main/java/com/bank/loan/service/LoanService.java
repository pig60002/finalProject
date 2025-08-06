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

/**
 * 貸款服務類別，負責貸款申請、新增貸款紀錄，以及檔案上傳相關業務邏輯。
 */
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

    /**
     * 取得指定會員的所有帳戶列表。
     * 
     * @param mId 會員ID
     * @return 該會員的帳戶清單
     */
    public List<Account> getAccountsByMemberId(Integer mId) {
        return accountRepo.findByMId(mId);
    }

    /**
     * 根據貸款類型與期數，產生結構化且唯一的 LoanId。
     * 格式範例：72 + loanTypeCode + termTypeCode + 5位流水號 + 1位隨機數字
     * 
     * @param loanTypeId 貸款類型ID（如 LT001）
     * @param loanTerm 貸款期數（月）
     * @return 生成的 LoanId 字串
     */
    private String generateStructuredLoanId(String loanTypeId, int loanTerm) {
        String bankCode = "7";      // 固定銀行代碼
        String accountType = "2";   // 貸款產品代號
        String loanTypeCode;
        String termTypeCode;

        // 根據 loanTypeId 轉換成代碼字串
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
                loanTypeCode = "9"; // 其他類型預設碼
                break;
        }

        // 根據期數分類代碼：12 個月以下為短期，13-36 月為中期，超過為長期
        if (loanTerm <= 12) {
            termTypeCode = "1"; // 短期
        } else if (loanTerm <= 36) {
            termTypeCode = "2"; // 中期
        } else {
            termTypeCode = "3"; // 長期
        }

        // 前綴字串組合
        String prefix = bankCode + accountType + loanTypeCode + termTypeCode;

        // 從資料庫查詢現有最大流水號（需在 LoanRepository 裡實作此方法）
        String maxSerial = loanRepo.findMaxSerialNo();

        // 若無資料則從1開始，否則流水號+1
        int serial = (maxSerial == null) ? 1 : Integer.parseInt(maxSerial) + 1;

        // 將流水號格式化為 5 位數，不足補0
        String serialFormatted = String.format("%05d", serial);

        // 產生 0~9 之間的隨機數字
        int randomDigit = (int) (Math.random() * 10);

        // 組合完整 LoanId 字串並回傳
        return prefix + serialFormatted + randomDigit;
    }

    /**
     * 建立新的貸款申請，包含上傳收入證明檔案，並儲存至資料庫。
     * 
     * @param dto 貸款申請 DTO，包含貸款資訊與申請人資料
     * @param file 上傳的收入證明檔案
     * @return 新增的 Loans 實體物件
     * @throws IOException 檔案處理例外
     */
    public Loans createLoan(LoanCreateDto dto, MultipartFile file) throws IOException {
        // 檔案空檔檢查，若空則拋出例外
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("上傳的檔案為空");
        }

        // 驗證還款帳戶是否屬於該會員，確保資料正確
        Account selectedAccount = accountRepo.findByAccountId(dto.getRepayAccountId());
        if (selectedAccount == null || !selectedAccount.getmId().equals(dto.getMId())) {
            throw new IllegalArgumentException("所選帳戶不存在或不屬於該會員");
        }

        // 設定檔案上傳路徑，預設存放於 C:/bankSpringBoot/Bank/uploadImg/loanImg/
        String baseUploadDir = "C:/bankSpringBoot/Bank/uploadImg/loanImg/";
        String uploadPath = baseUploadDir;
        File dir = new File(uploadPath);
        if (!dir.exists()) {
            dir.mkdirs();  // 若資料夾不存在，則建立
        }

        // 取得上傳檔案的副檔名（包含點號，例如 .pdf）
        String originalFilename = file.getOriginalFilename();
        String fileExtension = "";
        int dotIndex = originalFilename.lastIndexOf('.');
        if (dotIndex >= 0) {
            fileExtension = originalFilename.substring(dotIndex);
        }

        // 產生檔名，格式為 loanId_時間戳記.副檔名
        String loanId = generateStructuredLoanId(dto.getLoanTypeId(), dto.getLoanTerm());
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String fileName = loanId + "_" + timestamp + fileExtension;

        // 實際檔案儲存路徑
        File dest = new File(dir, fileName);
        file.transferTo(dest);  // 將檔案寫入磁碟

        // 設定 DTO 中檔案相對路徑，方便資料庫紀錄與前端存取
        dto.setIncomeProofPath("/uploadImg/loanImg/" + fileName);

        // 從資料庫取得貸款類型與期數詳細資料（含利率）
        LoanType loanType = loanTypeRepo.findById(dto.getLoanTypeId())
                .orElseThrow(() -> new IllegalArgumentException("找不到指定的貸款類型"));

        LoanTerm loanTerm = loanTermRepo.findById(dto.getLoanTermId())
                .orElseThrow(() -> new IllegalArgumentException("找不到指定的貸款期數"));

        // 計算利率 = 基準利率 + 期數加成利率
        BigDecimal baseRate = loanType.getBaseInterestRate();
        BigDecimal adjustmentRate = loanTerm.getTermAdjustmentRate();

        // 建立 Loans 實體並設定欄位
        Loans loan = new Loans();
        loan.setLoanId(generateStructuredLoanId(dto.getLoanTypeId(), dto.getLoanTerm()));
        loan.setLoanTypeId(dto.getLoanTypeId());
        loan.setLoanTermId(dto.getLoanTermId());
        loan.setLoanTerm(dto.getLoanTerm());
        loan.setMid(dto.getMId());
        loan.setLoanAmount(dto.getLoanAmount());
        loan.setLoanstartDate(LocalDate.now());
        loan.setApprovalStatus("pending");  // 預設狀態為待審核
        loan.setInterestRate(baseRate.add(adjustmentRate));  // 設定總利率
        loan.setRepayAccountId(dto.getRepayAccountId());
        loan.setProofDocumentUrl(dto.getIncomeProofPath());  // 記錄收入證明檔案位置

        // 設定建立與更新時間
        LocalDateTime now = LocalDateTime.now();
        loan.setCreatedAt(now);
        loan.setUpdatedAt(now);

        // 儲存貸款資料至資料庫並回傳實體
        return loanRepo.save(loan);
    }
}
