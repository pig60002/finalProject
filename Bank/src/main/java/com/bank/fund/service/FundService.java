package com.bank.fund.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bank.fund.entity.Fund;
import com.bank.fund.entity.FundHoldings;
import com.bank.fund.repository.FundRepository;

@Service
public class FundService {

	@Autowired
	private FundRepository fundRepository;
	
	@Transactional(readOnly = true)
	public List<Fund> getAll(){
		return fundRepository.findAll();
	}
	
	@Transactional(readOnly = true)
	public Optional<Fund> getById(Integer id) {
		return fundRepository.findById(id);
	}
	
	@Transactional
	public Fund create(Fund fund) {
		return fundRepository.save(fund);
	}
	
	@Transactional
	public Fund update(Integer id, Fund updatedFund) {
		Fund fund = fundRepository.findById(id).orElseThrow();
		if (updatedFund.getStatus() != null) {
			fund.setStatus(updatedFund.getStatus());
		}
		return fundRepository.save(fund);
	}

}
