package com.bank.loan.dao;


import org.springframework.data.jpa.repository.JpaRepository;

import com.bank.loan.bean.LoanTerm;

public interface LoanTermRepository extends JpaRepository<LoanTerm, String>{

}
