package com.bank.fund.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bank.account.bean.Transactions;
import com.bank.account.service.transactions.InternalTransferService;
import com.bank.fund.entity.FundHoldings;
import com.bank.fund.entity.FundNav;
import com.bank.fund.entity.FundTransaction;
import com.bank.fund.repository.FundHoldingsRepository;
import com.bank.fund.repository.FundNavRepository;
import com.bank.fund.repository.FundTransactionRepository;

@Service
public class FundTransactionService {

	@Autowired
	private FundTransactionRepository fundTransactionRepository;

	@Autowired
	private FundHoldingsRepository fundHoldingsRepository;

	@Autowired
	private FundNavRepository fundNavRepository;

	@Autowired
	private InternalTransferService internalTransferService;

	@Transactional(readOnly = true)
	public List<FundTransaction> getByFundAccId(Integer fundAccId) {
		return fundTransactionRepository.findByFundAccountFundAccId(fundAccId);
	}

	@Transactional
	public FundTransaction buyFund(FundTransaction fundTransaction) {

		Transactions transactions = new Transactions();
		transactions.setAccountId(fundTransaction.getFundAccount().getAccount().getAccountId());
		transactions.setTransactionType("基金申購");
		transactions.setToAccountId(fundTransaction.getFund().getCompanyAccount().getAccountId());
		transactions.setAmount(fundTransaction.getAmount());
		transactions.setOperatorId(1);
		transactions.setMemo("");

		transactions = internalTransferService.internalTransferAction(transactions);
		fundTransaction.setTransactions(transactions);

		return fundTransactionRepository.save(fundTransaction);
	}

	@Transactional
	public FundTransaction sellFund(FundTransaction fundTransaction) {

		FundHoldings fundHoldings = fundHoldingsRepository.findByFundAccountFundAccIdAndFundFundId(
				fundTransaction.getFundAccount().getFundAccId(), fundTransaction.getFund().getFundId()).orElseThrow();

		if (fundTransaction.getUnits().compareTo(fundHoldings.getUnits()) > 0) {
			return null;
		} else if (fundTransaction.getUnits().compareTo(fundHoldings.getUnits()) == 0) {
			fundHoldingsRepository.delete(fundHoldings);
		} else {
			fundHoldings.setUnits(fundHoldings.getUnits().subtract(fundTransaction.getUnits()));
			fundHoldings.setUpdateTime(LocalDateTime.now());
			fundHoldingsRepository.save(fundHoldings);
		}

		return fundTransactionRepository.save(fundTransaction);
	}
	
	
//	@Transactional
//    public FundTransaction executeTransaction(FundTransaction fundTransaction) {
//		// 取得最新 NAV
//        FundNav latestNav = fundNavRepository
//                .findTopByFundOrderByNavDateDesc(fundTransaction.getFund())
//                .orElseThrow(() -> new RuntimeException("尚無最新淨值"));
//
//        // 計算買進單位數
//        BigDecimal units = amount.divide(latestNav.getNavValue(), 4, RoundingMode.HALF_UP);
//        fundTransaction.setTranTime(LocalDateTime.now());
//        fundTransaction.setTranType("SIP");
//
//        return fundTransactionRepository.save(fundTransaction);
//    }
	
	@Transactional
	public FundTransaction agreeBuyFund(Integer id, FundTransaction updatedFundTransaction) {
		FundTransaction fundTransaction = fundTransactionRepository.findById(id).orElseThrow();

		BigDecimal fee = fundTransaction.getAmount().multiply(fundTransaction.getFund().getBuyFee());
		fundTransaction.setFee(fee);
		fundTransaction.setNav(updatedFundTransaction.getNav());

		BigDecimal units = fundTransaction.getAmount().subtract(fee);
		units = units.divide(fundTransaction.getNav());
		fundTransaction.setUnits(units);

		Optional<FundHoldings> OptionalFundHoldings = fundHoldingsRepository.findByFundAccountFundAccIdAndFundFundId(
				fundTransaction.getFundAccount().getFundAccId(), fundTransaction.getFund().getFundId());

		FundHoldings fundHoldings = new FundHoldings();

		// 將申購金額計入成本
		BigDecimal cost = fundTransaction.getAmount();

		if (OptionalFundHoldings.isPresent()) {
			fundHoldings = OptionalFundHoldings.get();

			// 總成本=申購金額+原單位成本*原持有單位
			cost = cost.add(fundHoldings.getCost().multiply(fundHoldings.getUnits()));

			units = units.add(fundHoldings.getUnits());

		} else {
			fundHoldings.setFundAccount(fundTransaction.getFundAccount());
			fundHoldings.setFund(fundTransaction.getFund());
		}
		// 單位成本=總成本/總持有單位
		cost = cost.divide(units);

		fundHoldings.setUnits(units);
		fundHoldings.setCost(cost);
		fundHoldings.setUpdateTime(LocalDateTime.now());
		fundHoldingsRepository.save(fundHoldings);

		fundTransaction.setTranTime(LocalDateTime.now());
		fundTransaction.setStatus("交易成功");

		return fundTransactionRepository.save(fundTransaction);
	}

	@Transactional
	public FundTransaction agreeSellFund(Integer id, FundTransaction updatedFundTransaction) {
		FundTransaction fundTransaction = fundTransactionRepository.findById(id).orElseThrow();
		
		fundTransaction.setNav(updatedFundTransaction.getNav());
		fundTransaction.setAmount(fundTransaction.getNav().multiply(fundTransaction.getUnits()));
		
		Transactions transactions = new Transactions();
		transactions.setAccountId(fundTransaction.getFund().getCompanyAccount().getAccountId());
		transactions.setTransactionType("基金贖回");
		transactions.setToAccountId(fundTransaction.getFundAccount().getAccount().getAccountId());
		transactions.setAmount(fundTransaction.getAmount());
		transactions.setOperatorId(1);
		transactions.setMemo("");

		transactions = internalTransferService.internalTransferAction(transactions);
		fundTransaction.setTransactions(transactions);
		
		fundTransaction.setTranTime(LocalDateTime.now());
		fundTransaction.setStatus("交易成功");

		return fundTransactionRepository.save(fundTransaction);
	}




}
