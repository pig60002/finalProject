package com.bank.fund.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bank.fund.entity.Fund;
import com.bank.fund.entity.FundNav;
import com.bank.fund.repository.FundNavRepository;

@Service
public class FundNavService {

	@Autowired
	private FundNavRepository fundNavRepository;
	
	@Transactional(readOnly = true)
	public List<FundNav> getByFundId(Integer fundId){
		return fundNavRepository.findByFundFundId(fundId);
	}
	
	@Transactional(readOnly = true)
	public Optional<FundNav> getById(Integer id) {
		return fundNavRepository.findById(id);
	}
	
	@Transactional
	public FundNav create(FundNav fundNav) {
		return fundNavRepository.save(fundNav);
	}
	
	@Transactional
	public void updateDailyNav(Fund fund) {
        // 模擬 NAV (實務應從 API 抓資料)
        BigDecimal nav = BigDecimal.valueOf(Math.random() * 10 + 10);

        FundNav fundNav = new FundNav();
        fundNav.setFund(fund);
        fundNav.setNavDate(LocalDate.now());
        fundNav.setNav(nav);

        fundNavRepository.save(fundNav);
    }
}
