package com.bank.loan.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bank.loan.bean.LoanType;

public interface LoanTypeRepository extends JpaRepository<LoanType, String> {

}
