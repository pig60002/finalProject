package com.bank.creditCard.transaction.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bank.creditCard.issue.dao.CardDetailRepository;
import com.bank.creditCard.issue.model.CardDetailBean;
import com.bank.creditCard.transaction.dao.CreditBillRepository;
import com.bank.creditCard.transaction.dao.CreditTransactionRepository;
import com.bank.creditCard.transaction.model.CreditBillBean;
import com.bank.creditCard.transaction.model.CreditTransactionBean;

@Service
public class CreditBillService {
	
	@Autowired
	private CreditBillRepository creditBillRepository;
	
	@Autowired
	private CreditTransactionRepository creditTransactionRepository;

	@Autowired
	private CardDetailRepository cardDetailRepository;
	
	public CreditBillBean generateBill(Integer cardId,String billingYearMonth) {
		CardDetailBean card=cardDetailRepository.findById(cardId)
				.orElseThrow(()->new RuntimeException("卡片不存在,ID="+cardId));
		YearMonth ym = YearMonth.parse(billingYearMonth,DateTimeFormatter.ofPattern("yyyyMM"));
		LocalDate startDate = ym.atDay(1);
		LocalDate endDate = ym.atEndOfMonth();
		LocalDateTime startDateTime = startDate.atStartOfDay();
		LocalDateTime endDateTime = endDate.atTime(23,59,59);
		
		//檢查是否已經存在該月份的帳單
		boolean exists = creditBillRepository.existsByCardDetailCardIdAndBillingDate(cardId, endDate);
		if(exists) {
			throw new RuntimeException("該卡片"+cardId+"在"+billingYearMonth+"已存在");
		}
		//取得該卡片該期間的所有交易(含退款)
		List<CreditTransactionBean> transactions=creditTransactionRepository.findByCardDetailCardIdAndTransactionTimeBetween(
		        cardId, startDateTime, endDateTime
			    );
		
		//計算總金額
		BigDecimal totalAmount = transactions.stream()
				.map(CreditTransactionBean::getAmount)
				.reduce(BigDecimal.ZERO, BigDecimal::add);
		
		//最低應繳10%
		BigDecimal minimumPayment = totalAmount.multiply(new BigDecimal("0.1"))
	            .setScale(2, BigDecimal.ROUND_HALF_UP);
		// 預設已繳金額 0
        BigDecimal paidAmount = BigDecimal.ZERO;
        
        // 帳單日期 = 該月份最後一天
        LocalDate billingDate = endDate;
        // 繳款截止日 
        LocalDate dueDate = billingDate.plusDays(20);
        
        CreditBillBean bill = new CreditBillBean();
        bill.setCardDetail(card);
        bill.setBillingDate(billingDate);
        bill.setDueDate(dueDate);
        bill.setTotalAmount(totalAmount);
        bill.setMinimumPayment(minimumPayment);
        bill.setPaidAmount(paidAmount);
        bill.setStatus("未繳");
        
        // 存帳單
        CreditBillBean savedBill = creditBillRepository.save(bill);

        // 將所有該期間交易更新關聯帳單 (如果你需要連結交易到帳單)
        for (CreditTransactionBean tx : transactions) {
            tx.setCreditBill(savedBill);
        }
        creditTransactionRepository.saveAll(transactions);

        return savedBill;
	}
	
	public List<CreditBillBean> findAllBills(){
		return creditBillRepository.findAll();
	}
	
	public List<CreditBillBean> findBillsByMemberId(Integer memberId){
		return creditBillRepository.findByCardDetailMemberMId(memberId);
	}
	
	public CreditBillBean findBillDetail(Integer billId) {
		return creditBillRepository.findById(billId).orElseThrow(()->new RuntimeException("帳單不存在,ID="+billId));
	}
}
