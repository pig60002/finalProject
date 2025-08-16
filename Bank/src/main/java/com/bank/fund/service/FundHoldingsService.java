package com.bank.fund.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bank.fund.entity.FundHoldings;
import com.bank.fund.repository.FundHoldingsRepository;

@Service
public class FundHoldingsService {

    @Autowired
    private FundHoldingsRepository fundHoldingsRepository;
	
	@Transactional(readOnly = true)
	public List<FundHoldings> getByFundAccId(Integer fundAccId){
		return fundHoldingsRepository.findByFundAccountFundAccId(fundAccId);
	}
	
	@Transactional
	public FundHoldings create(FundHoldings fundHoldings) {
		return fundHoldingsRepository.save(fundHoldings);
	}
	
	@Transactional
	public FundHoldings update(Integer id, FundHoldings updatedFundHoldings) {
		FundHoldings fundHoldings = fundHoldingsRepository.findById(id).orElseThrow();
		
		if (updatedFundHoldings.getUnits() != null) {
			fundHoldings.setUnits(updatedFundHoldings.getUnits());
		}
		if (updatedFundHoldings.getCost() != null) {
			fundHoldings.setCost(updatedFundHoldings.getCost());
		}
		fundHoldings.setUpdateTime(LocalDateTime.now());
		
		return fundHoldingsRepository.save(fundHoldings);
	}
    
}
