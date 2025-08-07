package com.bank.loan.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bank.loan.bean.Loans;
import com.bank.loan.dao.LoanRepository;
import com.bank.loan.dto.LoansDto;

/**
 * LoansDtoService 是一個服務層類別，
 * 負責從資料庫取得 Loans 實體資料並轉換為 LoansDto，
 * 以供前端或其他應用層使用。
 */
@Service
@Transactional
public class LoansDtoService {

    // 自動注入 LoanRepository，用來存取 Loans 資料
    @Autowired
    private LoanRepository lRepos;

    /**
     * 取得所有貸款資料並轉換為 LoansDto 列表。
     * 
     * @return 包含所有貸款資料的 DTO 清單
     */
    public List<LoansDto> findAllDto() {
        // 從資料庫中查詢所有貸款資料
        List<Loans> loans = lRepos.findAll();

        // 建立一個空的 DTO 清單，用來儲存轉換後的資料
        List<LoansDto> loanDtos = new ArrayList<>();

        // 將每一筆 Loans 實體資料轉換為 LoansDto 並加入清單中
        for (Loans loan : loans) {
            LoansDto dto = new LoansDto(loan); // 使用 LoansDto 的建構子來轉換
            loanDtos.add(dto);
        }

        // 回傳 DTO 清單
        return loanDtos;
    }
}
