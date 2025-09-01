package com.bank.fund.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
import com.bank.fund.repository.*;

@Service
public class FundTransactionService {

    private final FundRepository fundRepository;

    private final FundAccountRepository fundAccountRepository;

	@Autowired
	private FundNavRepository fundNavRepository;
    
	@Autowired
	private FundTransactionRepository fundTransactionRepository;

	@Autowired
	private FundHoldingsRepository fundHoldingsRepository;

	@Autowired
	private InternalTransferService internalTransferService;

    FundTransactionService(FundAccountRepository fundAccountRepository, FundRepository fundRepository) {
        this.fundAccountRepository = fundAccountRepository;
        this.fundRepository = fundRepository;
    }

	@Transactional(readOnly = true)
	public List<FundTransaction> getAll() {
		return fundTransactionRepository.findAllByOrderByFundTranIdDesc();
	}

	@Transactional(readOnly = true)
	public List<FundTransaction> getByFundAccId(Integer fundAccId) {
		return fundTransactionRepository.findByFundAccountFundAccId(fundAccId);
	}
	
	@Transactional(readOnly = true)
	public List<FundTransaction> getByStatus(String status) {
		return fundTransactionRepository.findByStatus(status);
	}
	
	@Transactional
	public FundTransaction buyFund(FundTransaction fundTransaction) {

		Transactions transactions = new Transactions();
		transactions.setAccountId(fundAccountRepository.findById(
				fundTransaction.getFundAccount().getFundAccId()).orElseThrow()
				.getAccount().getAccountId());
		transactions.setTransactionType("基金扣款");
		transactions.setToAccountId(fundRepository.findById(
				fundTransaction.getFund().getFundId()).orElseThrow()
				.getAccount().getAccountId());
		transactions.setAmount(fundTransaction.getAmount());
		transactions.setOperatorId(1);
		transactions.setMemo("");

		transactions = internalTransferService.internalTransferAction(transactions);
		if(transactions.getStatus() != "轉帳成功")
			return null;
		
		fundTransaction.setTransactions(transactions);
		fundTransaction.setTranType("申購");
		fundTransaction.setStatus("待審核");
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
		fundTransaction.setTranType("贖回");
		fundTransaction.setStatus("待審核");
		return fundTransactionRepository.save(fundTransaction);
	}
	
	@Transactional
	public FundTransaction agreeBuyFund(Integer id) {
	    try {
	        // 從資料庫載入原始交易記錄
	        FundTransaction fundTransaction = fundTransactionRepository.findById(id)
	            .orElseThrow(() -> new IllegalArgumentException("找不到交易記錄 ID: " + id));
	        

			FundNav fundNav = fundNavRepository.findTopByFundFundIdOrderByNavDateDesc(fundTransaction.getFund().getFundId()).orElseThrow();
			fundTransaction.setNav(fundNav.getNav());


	        // 驗證交易金額
	        if (fundTransaction.getAmount() == null || fundTransaction.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
	            throw new IllegalStateException("交易金額無效");
	        }

	        // 驗證基金手續費
	        if (fundTransaction.getFund() == null || fundTransaction.getFund().getBuyFee() == null) {
	            throw new IllegalStateException("基金資料或手續費無效");
	        }

	        // 計算手續費
	        BigDecimal fee = fundTransaction.getAmount()
	        		.multiply(fundTransaction.getFund().getBuyFee())
	        		.divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP);
	        fundTransaction.setFee(fee);

	        // 計算實際投資金額（扣除手續費）
	        BigDecimal investmentAmount = fundTransaction.getAmount().subtract(fee);
	        if (investmentAmount.compareTo(BigDecimal.ZERO) <= 0) {
	            throw new IllegalStateException("扣除手續費後的投資金額必須大於零");
	        }

	        // 計算購買單位數
	        BigDecimal purchasedUnits = investmentAmount.divide(fundTransaction.getNav(), 4, RoundingMode.HALF_UP);
	        fundTransaction.setUnits(purchasedUnits);

	        // 處理基金持倉
	        Optional<FundHoldings> existingHoldings = fundHoldingsRepository
	            .findByFundAccountFundAccIdAndFundFundId(
	                fundTransaction.getFundAccount().getFundAccId(), 
	                fundTransaction.getFund().getFundId()
	            );

	        FundHoldings holdings;
	        BigDecimal totalInvestment = fundTransaction.getAmount();
	        BigDecimal totalUnits = purchasedUnits;

	        if (existingHoldings.isPresent()) {
	            // 更新現有持倉
	            holdings = existingHoldings.get();
	            
	            // 安全地取得現有數據，避免 null
	            BigDecimal existingUnits = holdings.getUnits() != null ? holdings.getUnits() : BigDecimal.ZERO;
	            BigDecimal existingCost = holdings.getCost() != null ? holdings.getCost() : BigDecimal.ZERO;
	            
	            // 計算總投資金額 = 這次投資 + (之前的平均成本 × 之前的持有單位)
	            totalInvestment = totalInvestment.add(existingCost.multiply(existingUnits));
	            totalUnits = purchasedUnits.add(existingUnits);
	        } else {
	            // 建立新的持倉記錄
	            holdings = new FundHoldings();
	            holdings.setFundAccount(fundTransaction.getFundAccount());
	            holdings.setFund(fundTransaction.getFund());
	        }

	        // 防止除零錯誤
	        if (totalUnits.compareTo(BigDecimal.ZERO) == 0) {
	            throw new IllegalStateException("總持有單位數不能為零");
	        }

	        // 計算新的平均成本
	        BigDecimal averageCost = totalInvestment.divide(totalUnits, 4, RoundingMode.HALF_UP);

	        // 更新持倉資料
	        holdings.setUnits(totalUnits);
	        holdings.setCost(averageCost);
	        holdings.setUpdateTime(LocalDateTime.now());
	        fundHoldingsRepository.save(holdings);

	        // 更新交易狀態
	        fundTransaction.setTranTime(LocalDateTime.now());
	        fundTransaction.setStatus("交易成功");

	        return fundTransactionRepository.save(fundTransaction);

	    } catch (Exception e) {
	        // 記錄詳細的錯誤資訊
	        System.err.println("審核申購失敗 - 交易ID: " + id);
	        System.err.println("錯誤詳情: " + e.getMessage());
	        
	        // 重新拋出異常
	        throw new RuntimeException("審核申購失敗: " + e.getMessage(), e);
	    }
	}
	@Transactional
	public FundTransaction agreeSellFund(Integer id) {
		FundTransaction fundTransaction = fundTransactionRepository.findById(id).orElseThrow();
		if(fundTransaction.getStatus() == "交易成功")
			return null;
		
		FundNav fundNav = fundNavRepository.findTopByFundFundIdOrderByNavDateDesc(fundTransaction.getFund().getFundId()).orElseThrow();
		fundTransaction.setNav(fundNav.getNav());
		
		fundTransaction.setAmount(fundTransaction.getNav().multiply(fundTransaction.getUnits()));
		
		Transactions transactions = new Transactions();
		transactions.setAccountId(fundTransaction.getFund().getAccount().getAccountId());
		transactions.setTransactionType("基金扣款");
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
