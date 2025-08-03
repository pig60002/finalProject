package com.bank.fund.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bank.fund.entity.FundHoldings;

public interface FundHoldingsRepository extends JpaRepository<FundHoldings, Integer> {

}
