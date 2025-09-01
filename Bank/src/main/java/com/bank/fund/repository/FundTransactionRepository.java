package com.bank.fund.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bank.fund.entity.FundTransaction;

public interface FundTransactionRepository extends JpaRepository<FundTransaction, Integer> {

	List<FundTransaction> findByFundAccountFundAccId(Integer fundAccId);
	
	List<FundTransaction> findByStatus(String status);
	
	List<FundTransaction> findAllByOrderByFundTranIdDesc();
}