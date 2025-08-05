package com.bank.loan.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.bank.loan.bean.Loans;

public interface LoanRepository extends JpaRepository<Loans, String> {
	@Query("SELECT MAX(SUBSTRING(l.loanId, 5, 5)) FROM Loans l")
	String findMaxSerialNo();

}
