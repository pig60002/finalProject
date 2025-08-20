package com.bank.fund.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bank.fund.entity.FundSip;

public interface FundSipRepository extends JpaRepository<FundSip, Integer> {

	List<FundSip> findByFundAccountFundAccId(Integer fundAccId);

	List<FundSip> findByStatus(String status);

}
