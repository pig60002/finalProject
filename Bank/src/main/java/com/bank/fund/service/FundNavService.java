package com.bank.fund.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bank.fund.dto.FundDto;
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
	public void updateDailyNav() {
		List<FundDto> fundDtos = fundRepository.findAllFundsWithLatestNav();
		BigDecimal nav;
		for (FundDto fundDto : fundDtos) {
			// 模擬 NAV (實務應從 API 抓資料)
			// 隨機浮點數 0 ~ 1
	        double randomDouble = (ThreadLocalRandom.current().nextDouble() - 0.5)
	        		* ThreadLocalRandom.current().nextDouble() * 10;
	        nav = fundDto.getLatestNav().add(BigDecimal.valueOf(randomDouble));
	        
			FundNav fundNav = new FundNav();
//			fundNav.setFund();
			fundNav.setNavDate(LocalDate.now());
			fundNav.setNav(nav);
			
			fundNavRepository.save(fundNav);			
		}
    }
}
