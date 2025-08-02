package com.bank.loan.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.bank.loan.bean.Loans;
import com.bank.loan.dto.LoanCreateDto;
import com.bank.loan.service.LoanService;

@RestController
@RequestMapping("/loans")
public class LoanController {

    @Autowired
    private LoanService loanService;

    /**
     * 新增貸款（一定會有檔案）
     * 前端表單請用 enctype="multipart/form-data"
     * 欄位名稱需為：loan, file
     */
    @PostMapping("/create")
    public ResponseEntity<?> createLoan(
            @RequestPart("loan") LoanCreateDto dto,
            @RequestPart("file") MultipartFile file) {

        try {
            Loans savedLoan = loanService.createLoan(dto, file);
            return ResponseEntity.ok(savedLoan);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("檔案上傳失敗");
        } catch (Exception e) {
            e.printStackTrace(); // 開發用，之後可拿掉
            return ResponseEntity.internalServerError().body("新增貸款失敗");
        }
    }
}
