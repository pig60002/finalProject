package com.bank.loan.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bank.loan.bean.Loans;
import com.bank.loan.dao.LoanRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class LoansService {
	
	@Autowired
	private LoanRepository lRepos;

	public List<Loans> findAlldata(){
		return lRepos.findAll();
	}
	
}
