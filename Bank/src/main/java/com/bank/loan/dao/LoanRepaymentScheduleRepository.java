package com.bank.loan.dao;

import com.bank.loan.bean.LoanRepaymentSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LoanRepaymentScheduleRepository extends JpaRepository<LoanRepaymentSchedule, Long> {
    List<LoanRepaymentSchedule> findByLoanId(String loanId);
    
 // 新增：查詢指定貸款且狀態為 pending 的排程
    List<LoanRepaymentSchedule> findByLoanIdAndPaymentStatus(String loanId, String paymentStatus);

}
