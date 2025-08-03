package com.bank.fund.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bank.fund.entity.FundTransaction;

public interface FundTransactionRepository extends JpaRepository<FundTransaction, Integer> {

}