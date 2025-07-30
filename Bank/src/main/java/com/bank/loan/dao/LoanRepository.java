package com.bank.loan.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bank.loan.bean.Loans;

public interface LoanRepository extends JpaRepository<Loans, String> {

}
