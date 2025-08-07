package com.bank.loan.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.bank.loan.bean.Loans;

public interface LoanRepository extends JpaRepository<Loans, String> {
	@Query("SELECT MAX(SUBSTRING(l.loanId, 5, 5)) FROM Loans l")
	String findMaxSerialNo();
	
	// 根據顧客姓名模糊查詢 (假設 Member 以 mName 存取)
    List<Loans> findByMember_mNameContainingIgnoreCase(String mName);

}
