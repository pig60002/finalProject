package com.bank.fund.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bank.fund.entity.FundSip;
import com.bank.fund.repository.FundSipRepository;

@Service
public class FundSipService {

	@Autowired
	private FundSipRepository fundSipRepository;
	
	@Transactional(readOnly = true)
	public List<FundSip> getByFundAccId(Integer fundAccId) {
		return fundSipRepository.findByFundAccountFundAccId(fundAccId);
	}
	
	@Transactional
	public FundSip create(FundSip fundSip) {
		return fundSipRepository.save(fundSip);
	}
	
	@Transactional
	public FundSip update(Integer id, FundSip updatedFundSip) {
		FundSip fundSip = fundSipRepository.findById(id).orElseThrow();
		
		if (updatedFundSip.getAmount() != null) {
			fundSip.setAmount(updatedFundSip.getAmount());
		}
		if (updatedFundSip.getFrequency() != null) {
			fundSip.setFrequency(updatedFundSip.getFrequency());
		}
		if (updatedFundSip.getStartDate() != null) {
			fundSip.setStartDate(updatedFundSip.getStartDate());
		}
		if (updatedFundSip.getEndDate() != null) {
			fundSip.setEndDate(updatedFundSip.getEndDate());
		}
		
		return fundSipRepository.save(fundSip);
	}
//	public void processDueSips() {
//        LocalDate today = LocalDate.now();
//        List<FundSip> activeSips = fundSipRepository.findByStatus("ACTIVE");
//
//        for (FundSip sip : activeSips) {
//            if (today.isBefore(sip.getStartDate())) continue;
//            if (sip.getEndDate() != null && today.isAfter(sip.getEndDate())) continue;
//
//            if (isExecutionDay(sip, today)) {
//                fundTransactionService.executeTransaction(
//                        sip.getFundAccount(),
//                        sip.getFund(),
//                        sip.getAmount()
//                );
//            }
//        }
//    }
//
//    private boolean isExecutionDay(FundSip sip, LocalDate today) {
//        switch (sip.getFrequency().toUpperCase()) {
//            case "DAILY": return true;
//            case "WEEKLY": return today.getDayOfWeek() == sip.getStartDate().getDayOfWeek();
//            case "MONTHLY": return today.getDayOfMonth() == sip.getStartDate().getDayOfMonth();
//            default: return false;
//        }
//    }
}
