package com.bank.fund.service;

import com.bank.account.bean.Account;
import com.bank.account.dao.AccountRepository;
import com.bank.fund.dto.FundAccountDto;
import com.bank.fund.entity.FundAccount;
import com.bank.fund.repository.FundAccountRepository;
import com.bank.member.bean.Member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FundAccountService {
	
	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private FundAccountRepository fundAccountRepository;

	public List<FundAccountDto> fundAccountDto(List<FundAccount> fundAccounts) {
		List<FundAccountDto> fundAccountDtos = new ArrayList<FundAccountDto>();

		for (FundAccount fundAccount : fundAccounts) {
			FundAccountDto fundAccountDto = new FundAccountDto();

			fundAccountDto.setId(fundAccount.getId());
			fundAccountDto.setName(fundAccount.getMember().getmName());
			fundAccountDto.setRiskType(fundAccount.getRiskType());
			fundAccountDto.setStatus(fundAccount.getStatus());

			fundAccountDtos.add(fundAccountDto);
		}
		return fundAccountDtos;
	}

	@Transactional(readOnly = true)
	public List<FundAccountDto> getAll() {
		return fundAccountDto(fundAccountRepository.findAll());
	}

	@Transactional(readOnly = true)
	public List<FundAccountDto> getByStatus(String status) {
		return fundAccountDto(fundAccountRepository.findByStatus(status));
	}

//	@Transactional(readOnly = true)
//	public List<FundAccountDto> getByName(String name) {
//		return fundAccountDto(fundAccountRepository.findByMemberMNameContaining(name));
//	}

	@Transactional(readOnly = true)
	public Optional<FundAccount> getById(Integer id) {
		return fundAccountRepository.findById(id);
	}

	@Transactional
	public boolean create(Integer memberId, String riskType) {
		FundAccount fundAccount = new FundAccount();

		Member member = new Member();
		member.setmId(memberId);
		fundAccount.setMember(member);
		
		Account account = accountRepository
				.findByMIdAndAccountNameAndCurrency(memberId, "活期存款", "NT");
		fundAccount.setAccount(account);

		fundAccount.setRiskType(riskType);
		fundAccount.setStatus("審核中");

		fundAccountRepository.save(fundAccount);

		return true;
	}


	@Transactional
	public boolean update(Integer id, String riskType, String status) {
		if (!fundAccountRepository.existsById(id)) {
			return false;
		}
		FundAccount fundAccount = fundAccountRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("FundAccount not found"));

		if (riskType != null) {
			fundAccount.setRiskType(riskType);			
		}
		
		if (status != null) {
			fundAccount.setStatus(status);
		}
		
		fundAccountRepository.save(fundAccount);
		return true;
	}

	@Transactional
	public boolean delete(Integer id) {
		if (!fundAccountRepository.existsById(id)) {
			return false;
		}
		fundAccountRepository.deleteById(id);
		return true;
	}
}
