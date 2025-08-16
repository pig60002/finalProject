package com.bank.fund.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bank.fund.entity.FundAccount;
import com.bank.fund.entity.FundHoldings;

public interface FundHoldingsRepository extends JpaRepository<FundHoldings, Integer> {

	List<FundHoldings> findByFundAccountFundAccId(Integer fundAccId);
}
