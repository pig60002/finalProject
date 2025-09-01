package com.bank.fund.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import com.bank.fund.entity.Fund;
import com.bank.fund.entity.FundNav;
import com.bank.fund.repository.FundNavRepository;
import com.bank.fund.repository.FundRepository;

@Service
public class FundNavService {
    
    @Autowired
    private FundNavRepository fundNavRepository;
    
    @Autowired
    private FundRepository fundRepository;
    
    @Transactional
    public FundNav create(FundNav fundNav) {
        Optional<FundNav> existingNav = fundNavRepository
            .findByFundAndNavDate(fundNav.getFund(), fundNav.getNavDate());
        
        if (existingNav.isPresent()) {
            FundNav existing = existingNav.get();
            existing.setNav(fundNav.getNav());
            return fundNavRepository.save(existing);
        } else {
            return fundNavRepository.save(fundNav);
        }
    }
    
    @Transactional(readOnly = true)
    public Optional<FundNav> getLatestNavByFund(Fund fund) {
        return fundNavRepository.findTopByFundOrderByNavDateDesc(fund);
    }
    
    @Transactional(readOnly = true)
    public Optional<FundNav> getNavByFundAndDate(Fund fund, LocalDate date) {
        return fundNavRepository.findByFundAndNavDate(fund, date);
    }

}