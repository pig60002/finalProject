package com.bank.fund.service;


import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bank.fund.dto.FundHoldingsDto;
import com.bank.fund.entity.FundAccount;
import com.bank.fund.entity.FundHoldings;
import com.bank.fund.repository.FundAccountRepository;
import com.bank.fund.repository.FundHoldingsRepository;

@Service
public class FundHoldingsService {

	@Autowired
	private FundAccountRepository fundAccountRepository;

    @Autowired
    private FundHoldingsRepository fundHoldingsRepository;
    
    public List<FundHoldingsDto> fundHoldingsDto(List<FundHoldings> fundHoldingsList) {
		List<FundHoldingsDto> fundHoldingsDtos = new ArrayList<FundHoldingsDto>();

		for (FundHoldings fundHoldings : fundHoldingsList) {
			FundHoldingsDto fundHoldingsDto = new FundHoldingsDto();

			fundHoldingsDto.setId(fundHoldings.getHoldingId());
			fundHoldingsDto.setFundCode(fundHoldings.getFund().getFundCode());
			fundHoldingsDto.setFundName(fundHoldings.getFund().getFundName());
			fundHoldingsDto.setUnits(fundHoldings.getUnits());
			fundHoldingsDto.setCost(fundHoldings.getCost());

			fundHoldingsDtos.add(fundHoldingsDto);
		}
		return fundHoldingsDtos;
	}
    
    @Transactional
    public List<FundHoldingsDto> getByFundAccId(Integer fundAccId) {
    	FundAccount fundAccount = fundAccountRepository
    			.findById(fundAccId).orElseThrow(() -> new RuntimeException("FundAccount not found"));
		return fundHoldingsDto(fundHoldingsRepository.findByFundAccount(fundAccount));
	}
    
    @Transactional
    public boolean create(FundHoldings fundHoldings) {
    	fundHoldingsRepository.save(fundHoldings);
    	return true;
    }
    
    @Transactional
    public boolean update(FundHoldings fundHoldings) {
		if (!fundHoldingsRepository.existsById(fundHoldings.getHoldingId())) {
			return false;
		}
		FundHoldings updateBean = fundHoldingsRepository.findById(fundHoldings.getHoldingId())
				.orElseThrow(() -> new RuntimeException("FundHoldings not found"));
		
		if (fundHoldings.getUnits() != null) {
			updateBean.setUnits(fundHoldings.getUnits());
		}
		if (fundHoldings.getCost() != null) {
			updateBean.setCost(fundHoldings.getCost());
		}
		
		fundHoldingsRepository.save(updateBean);
		return true;
    }
    
    @Transactional
	public boolean delete(Integer id) {
		if (!fundHoldingsRepository.existsById(id)) {
			return false;
		}
		fundHoldingsRepository.deleteById(id);
		return true;
	}
}
