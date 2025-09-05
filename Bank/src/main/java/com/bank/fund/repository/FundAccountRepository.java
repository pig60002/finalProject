package com.bank.fund.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bank.fund.entity.FundAccount;


public interface FundAccountRepository extends JpaRepository<FundAccount, Integer> {
	
	Optional<FundAccount> findByMemberMId(Integer mId);
	
	List<FundAccount> findByStatus(String status);
	
	List<FundAccount> findByMemberMNameContaining(String name);

	List<FundAccount> findByStatusAndMemberMNameContaining(String status, String name);
	
}
