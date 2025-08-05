package com.bank.loan.dao;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bank.loan.bean.LoanTerm;

public interface LoanTermRepository extends JpaRepository<LoanTerm, String>{
	@Query("SELECT t FROM LoanTerm t WHERE :term BETWEEN t.minMonths AND t.maxMonths")
	Optional<LoanTerm> findByTermMonth(@Param("term") int term);

}
