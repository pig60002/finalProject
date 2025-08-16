package com.bank.fund.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bank.fund.entity.Fund;
import com.bank.fund.entity.FundSip;
import com.bank.fund.repository.FundSipRepository;

@Service
public class FundSipService {

	@Autowired
	private FundSipRepository fundSipRepository;
	
	@Transactional(readOnly = true)
	public Optional<FundSip> getById(Integer id) {
		return fundSipRepository.findById(id);
	}
	
	@Transactional
	public FundSip create(FundSip fundSip) {
		return fundSipRepository.save(fundSip);
	}
	
	@Transactional
	public FundSip update(Integer id, FundSip updatedfundSip) {
		FundSip fundSip = fundSipRepository.findById(id).orElseThrow();
		return fundSipRepository.save(fundSip);
	}
}
