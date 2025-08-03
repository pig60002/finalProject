package com.bank.fund.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bank.fund.entity.FundNav;

public interface FundNavRepository extends JpaRepository<FundNav, Integer> {

    List<FundNav> findByFundIdOrderByNavDateDesc(Integer fundId);

    FundNav findByFundIdAndNavDate(Integer fundId, LocalDate navDate);
}
