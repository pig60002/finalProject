package com.bank.fund.service;

import com.bank.account.bean.Account;
import com.bank.account.dao.AccountRepository;
import com.bank.fund.entity.FundAccount;
import com.bank.fund.repository.FundAccountRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class FundAccountService {
	
	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private FundAccountRepository fundAccountRepository;

	@Transactional(readOnly = true)
	public List<FundAccount> getAll(){
		return fundAccountRepository.findAll();
	}
	
	@Transactional(readOnly = true)
	public Optional<FundAccount> getById(Integer id) {
		return fundAccountRepository.findById(id);
	}
	
	@Transactional
	public FundAccount create(FundAccount fundAccount) {
        Account account = accountRepository.findByMIdAndAccountNameAndCurrency(fundAccount.getMember().getmId(), "活期存款", "NT");
        fundAccount.setAccount(account);
        return fundAccountRepository.save(fundAccount);
	}
	
	@Transactional
	public FundAccount update(Integer id, FundAccount updatedFundAccount) {
		FundAccount fundAccount = fundAccountRepository.findById(id).orElseThrow();
		
		if(updatedFundAccount.getRiskType() != null) {
			fundAccount.setRiskType(updatedFundAccount.getRiskType());			
		}
		if(updatedFundAccount.getStatus() != null) {
			fundAccount.setStatus(updatedFundAccount.getStatus());			
		}
		
		return fundAccountRepository.save(fundAccount);
	}
}
