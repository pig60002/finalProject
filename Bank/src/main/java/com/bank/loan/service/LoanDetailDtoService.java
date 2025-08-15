package com.bank.loan.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bank.loan.bean.CreditProfiles;
import com.bank.loan.bean.CreditReviewLogs;
import com.bank.loan.bean.Loans;
import com.bank.loan.dao.CreditProfilesRepository;
import com.bank.loan.dao.CreditReviewLogsRepository;
import com.bank.loan.dao.LoanRepository;
import com.bank.loan.dto.LoanDetailDto;
import com.bank.loan.dto.LoanDetailDtoMapper;
import com.bank.member.bean.Member;
import com.bank.member.dao.MemberRepository;


@Service
@Transactional
public class LoanDetailDtoService {
	
	@Autowired
	private LoanRepository lRepo;
	
	@Autowired
	private MemberRepository mRepo;
	
	@Autowired
	private CreditProfilesRepository cpRepo;
	
	@Autowired
	private CreditReviewLogsRepository crlRepo;
	
	/**
     * 根據 loanId 回傳貸款詳細資料
     */
	public LoanDetailDto findLoanDetailById(String loanId) {
		
		// 取得貸款主資料
		Loans loan = lRepo.findById(loanId).orElse(null);
		if (loan == null) {
		    return null;
		}

        // 根據 loan 取得 member
        Member member = mRepo.findById(loan.getMid()).get();

        // 根據 member mid 找信用資料
        CreditProfiles profile = cpRepo.findByMember_mId(loan.getMid());

        // 取得最新一筆審核紀錄
        Optional<CreditReviewLogs> crlOptional = crlRepo.findTopByLoanIdOrderByReviewTimeDesc(loanId);
        CreditReviewLogs crl = crlOptional.orElse(null);

        // 組裝成 DTO
        return LoanDetailDtoMapper.toDto(loan, member, profile, crl);
	}

}
