package com.bank.fund.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bank.fund.entity.Fund;


public interface FundRepository extends JpaRepository<Fund, Integer> {

    Fund findByFundCode(String fundCode);
}