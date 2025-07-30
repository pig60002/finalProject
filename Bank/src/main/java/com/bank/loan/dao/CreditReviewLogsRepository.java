package com.bank.loan.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bank.loan.bean.CreditReviewLogs;

public interface CreditReviewLogsRepository extends JpaRepository<CreditReviewLogs, Integer>{

}
