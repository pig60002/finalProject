package com.bank.loan.dao;

import com.bank.loan.bean.LoanPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LoanPaymentRepository extends JpaRepository<LoanPayment, Long> {
    List<LoanPayment> findByLoanId(String loanId);
    List<LoanPayment> findByScheduleId(Long scheduleId);
    
    // 依據會員 m_id 查詢所有貸款的繳費紀錄
    @Query("SELECT p FROM LoanPayment p WHERE p.loan.mId = :memberId")
    List<LoanPayment> findByMemberId(@Param("memberId") int mId);

}
