package com.bank.loan.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bank.loan.bean.Loans;
import com.bank.loan.dao.LoanRepository;
import com.bank.loan.dto.LoansDto;


@Service
@Transactional
public class LoansService {

	@Autowired
	private LoanRepository lRepos;

	public List<LoansDto> findAllDto() {
		List<Loans> loans = lRepos.findAll();
		List<LoansDto> loanDtos = new ArrayList<>();

		for (Loans loan : loans) {
			LoansDto dto = new LoansDto(loan);
			loanDtos.add(dto);
		}

		return loanDtos;
	}

}
