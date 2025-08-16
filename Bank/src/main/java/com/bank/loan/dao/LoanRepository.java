package com.bank.loan.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.bank.loan.bean.Loans;

public interface LoanRepository extends JpaRepository<Loans, String> {
	@Query("SELECT MAX(SUBSTRING(l.loanId, 5, 5)) FROM Loans l")
	String findMaxSerialNo();
	
	// 根據顧客姓名模糊查詢
    List<Loans> findByMember_mNameContainingIgnoreCase(String mName);
    
    // 根據貸款狀態查詢
    List<Loans> findByApprovalStatus(String approvalStatus);
    
 // 根據顧客姓名模糊 & 貸款狀態查詢
    List<Loans> findByMember_mNameContainingIgnoreCaseAndApprovalStatus(String mName, String approvalStatus);

}
