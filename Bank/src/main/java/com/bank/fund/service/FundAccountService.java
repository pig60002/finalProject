package com.bank.fund.service;

import com.bank.fund.dto.FundAccountDto;
import com.bank.fund.entity.FundAccount;
import com.bank.fund.repository.FundAccountRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FundAccountService {

	@Autowired
	private FundAccountRepository fundAccountRepository;

	public List<FundAccountDto> fundAccountDto(List<FundAccount> fundAccounts) {
		List<FundAccountDto> fundAccountDtos = new ArrayList<FundAccountDto>();

		for (FundAccount fundAccount : fundAccounts) {
			FundAccountDto fundAccountDto = new FundAccountDto();

			fundAccountDto.setId(fundAccount.getFundAccId());
			fundAccountDto.setMemberId(fundAccount.getMember().getmId());
			fundAccountDto.setName(fundAccount.getMember().getmName());
			fundAccountDto.setRiskType(fundAccount.getRiskType());
			fundAccountDto.setStatus(fundAccount.getStatus());

			fundAccountDtos.add(fundAccountDto);
		}
		return fundAccountDtos;
	}
	

	@Transactional(readOnly = true)
	public List<FundAccount> getAll() {
		return fundAccountRepository.findAll();
	}
	
	@Transactional(readOnly = true)
	public List<FundAccount> getByStatus(String status){
		return fundAccountRepository.findByStatus(status);
	}

	@Transactional(readOnly = true)
	public Optional<FundAccount> getById(Integer id) {
		return fundAccountRepository.findById(id);
	}

	@Transactional(readOnly = true)
	public Optional<FundAccount> getByMId(Integer mId) {
		return fundAccountRepository.findByMemberMId(mId);
	}

	public String createRiskType(List<Integer> riskAnswers) {
		Integer score = 0;
		for(Integer riskAnswer: riskAnswers) {
			score += riskAnswer;
		}
		
		if (score <= 0) {
			return "保守型";
		} else if(score <= 50) {
			return "穩健型";
		} else {
			return "積極型";
		}
	}

	@Transactional
	public FundAccount create(FundAccount fundAccount) {
		return fundAccountRepository.save(fundAccount);
	}

	@Transactional
	public FundAccount update(Integer id, FundAccount updatedFundAccount) {
		FundAccount fundAccount = fundAccountRepository.findById(id).orElseThrow();

		if (updatedFundAccount.getRiskType() != null) {
			fundAccount.setRiskType(updatedFundAccount.getRiskType());
		}
		if (updatedFundAccount.getStatus() != null) {
			fundAccount.setStatus(updatedFundAccount.getStatus());
		}

		return fundAccountRepository.save(fundAccount);
	}
}
