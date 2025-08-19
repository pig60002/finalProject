package com.bank.fund.service;

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
}
