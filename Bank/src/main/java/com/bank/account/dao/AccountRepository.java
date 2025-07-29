package com.bank.account.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bank.account.bean.Account;

import java.util.List;


public interface AccountRepository extends JpaRepository<Account, String> {
	
	List<Account> findByMId(Integer mId);

}
