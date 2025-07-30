package com.bank.loan.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bank.loan.bean.CreditProfiles;

public interface CreditProfilesRepository extends JpaRepository<CreditProfiles, Integer>{

}
