package com.bank.fund.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
	
	@Transactional
	public FundNav create(FundNav fundNav) {
		return fundNavRepository.save(fundNav);
	}

}
