package com.bank.fund.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bank.fund.entity.FundTransaction;
import com.bank.fund.repository.FundTransactionRepository;

@Service
public class FundTransactionService {

	@Autowired
	private FundTransactionRepository fundTransactionRepository;
	
	@Transactional(readOnly = true)
	public Optional<FundTransaction> getById(Integer id) {
		return fundTransactionRepository.findById(id);
	}
	
	@Transactional
	public FundTransaction create(FundTransaction fundTransaction) {
		return fundTransactionRepository.save(fundTransaction);
	}
	
	@Transactional
	public FundTransaction update(Integer id, FundTransaction updatedfundTransaction) {
		FundTransaction fundTransaction = fundTransactionRepository.findById(id).orElseThrow();
		
		return fundTransactionRepository.save(fundTransaction);
	}
}
