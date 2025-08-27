package com.bank.fund.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bank.fund.entity.Fund;
import com.bank.fund.repository.FundRepository;
import com.bank.fund.dto.FundDto;



@Service
public class FundService {

	@Autowired
	private FundRepository fundRepository;
	
	//查詢所有基金並包含最新淨值
	@Transactional(readOnly = true)
	public List<FundDto> getAll() {
		return fundRepository.findAllFundsWithLatestNav();
	}
	
	//根據條件查詢基金
	public List<FundDto> findByConditions(String fundType, Integer riskLevel, String status) {
		return fundRepository.findFundsByConditionsWithLatestNav(fundType, riskLevel, status);
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


